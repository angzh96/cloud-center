package com.intelligence.edge.server;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import com.intelligence.edge.pojo.CarUWBInfo;
import com.intelligence.edge.pojo.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.boot.configurationprocessor.json.JSONStringer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Data
@Slf4j(topic = "c.CarUWBServer")
public class CarUWBServer {
    private String deviceID;
    private int port;
    private Thread socketThread;
    private DatagramSocket socket;
    private volatile boolean runFlag = true;
    private Double LSB_M_TO_LAT_LONG = 0.000008993216059;
    private Double lng;
    private Double lat;

    public CarUWBServer (String deviceID, int port) throws SocketException {
        this.deviceID = deviceID;
        this.port = port;
    }

    public CarUWBServer (Double lng, Double lat, String deviceID){
        this.lat = lat;
        this.lng = lng;
        this.deviceID = deviceID;
    }

    public void onOpen(){
        Thread th = new Thread(new TaskV(lng, lat, deviceID));
        th.start();
    }

    /**
     * 开启数据接收服务端
     *
     * @throws SocketException
     */
    public void start() throws SocketException {

        runFlag = true;
        socket = new DatagramSocket(port);
        socketThread = new Thread(new Task(socket));     // 每接收到一个Socket就建立一个新的线程处理
        socketThread.start();
    }

    /**
     * 关闭数据接收服务端
     */
    public void end() {
        socket.close();
        runFlag = false;    //通过改变标志位让线程退出
    }

    /**
     * 数据接收线程
     */
    public class Task implements Runnable {
        private DatagramSocket socket;
        public Task(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            byte[] buf = new byte[2048];
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                DatagramPacket outPacket;
                log.info(deviceID + "UWB环境数据接收端打开：");
                while (runFlag){
                    socket.receive(packet);
                    String recvData = new String(packet.getData(), 0 , packet.getLength());
                    String printData = recvData + " from " + packet.getAddress().getHostAddress() + ":" + packet.getPort();
                    log.info(printData);
                    Gson gson = new Gson();
                    /*Double[] disarr = new Double[8];
                    //JsonArray arr = new JsonParser().parse(recvData).getAsJsonObject().getAsJsonArray("disarr");
                    JsonObject jsonObject = new JsonParser().parse(recvData).getAsJsonObject();
                    Double latitude = jsonObject.get("latitude").getAsDouble();
                    Double longtitude = jsonObject.get("longtitude").getAsDouble();
                    Double direction = jsonObject.get("direction").getAsDouble();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("disarr");
                    String time = jsonObject.get("time").getAsString();
                    for (int tmp = 0; tmp < 8; tmp ++) {
                        disarr[tmp] = jsonArray.get(tmp).getAsDouble();
                    }
                    CarUWBInfo cui = new CarUWBInfo(time, deviceID, longtitude, latitude, direction, disarr);
                    log.info(cui.getLatitude().toString());*/
                    CarUWBInfo cui = gson.fromJson(recvData, CarUWBInfo.class);
                    cui.setDeviceID(deviceID);
                    PosDeliverServer posDeliverServer = new PosDeliverServer();
                    Position newAnchor = posDeliverServer.deliverAnchorPos(cui.getLongitude(), cui.getLatitude(), cui.getDirection(), deviceID);
                    //使用字节流发送
                    /*ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(newAnchor);
                    oos.flush();
                    byte[] senddata = out.toByteArray();
                    outPacket = new DatagramPacket(senddata, senddata.length, packet.getSocketAddress());
                    socket.send(outPacket);*/
                    //WebSocketServer.sendInfo(recvData, "user1");
                    CarTempData.ccsMap.get(deviceID).senddata();
                    packet.setLength(buf.length);
                    //oos.close();
                    //out.close();
                }
            } catch (SocketException e) {
                return;
            } catch (IOException ei) {
                ei.printStackTrace();
            }
        }
    }

    public class TaskV implements Runnable {
        private Double lng;
        private Double lat;
        private String carID;
        private TaskV(Double lng, Double lat, String  carID) {
            this.lat = lat;
            this.lng = lng;
            this.carID = carID;
        }

        @Override
        public void run() {
            Double direction = 0.0;
            String aid = "A1";
            String did = "car1";
            Double alatitude = CarTempData.anchorPos.get(aid).getLatitude();
            Double alongitude = CarTempData.anchorPos.get(aid).getLongitude();
            CarUWBInfo pos = new CarUWBInfo("0", did, lng, lat, direction, alongitude, alatitude, aid);
            while (true) {
                Random random = new Random();
                int dirrd = random.nextInt(30);
                int rddir = random.nextInt(30);
                if (dirrd < 15) {
                    direction = direction - rddir;
                    if (direction < 0) {
                        direction = 360.0;
                    }
                } else if (dirrd >= 15 && dirrd < 29){
                    direction = direction + rddir;
                    if (direction > 360) {
                        direction = 0.0;
                    }
                } else {
                    direction = direction + 180;
                    if (direction > 360) {
                        direction = direction - 360;
                    }
                }
                int rdy = random.nextInt(2);
                int rdx = random.nextInt(2);
                if (direction == 0) {
                    lat = lat + LSB_M_TO_LAT_LONG * 5;
                }
                if (direction == 90) {
                    lng = lng + LSB_M_TO_LAT_LONG * 5;
                }
                if (direction == 180) {
                    lat = lat - LSB_M_TO_LAT_LONG * 5;
                }
                if (direction == 270) {
                    lat = lat - LSB_M_TO_LAT_LONG * 5;
                }
                if (direction > 0 && direction < 90) {
                    if (rdx == 1) {
                        lng = lng + LSB_M_TO_LAT_LONG * 5;
                    }
                    if (rdy == 1) {
                        lat = lat + LSB_M_TO_LAT_LONG * 5;
                    }
                }
                if (direction > 90 && direction < 180) {
                    if (rdx == 1) {
                        lng = lng + LSB_M_TO_LAT_LONG * 5;
                    }
                    if (rdy == 1) {
                        lat = lat - LSB_M_TO_LAT_LONG * 5;
                    }
                }
                if (direction > 180 && direction < 270) {
                    if (rdx == 1) {
                        lng = lng - LSB_M_TO_LAT_LONG * 5;
                    }
                    if (rdy == 1) {
                        lat = lat - LSB_M_TO_LAT_LONG * 5;
                    }
                }
                if (direction > 270 && direction < 360) {
                    if (rdx == 1) {
                        lng = lng - LSB_M_TO_LAT_LONG * 5;
                    }
                    if (rdy == 1) {
                        lat = lat + LSB_M_TO_LAT_LONG * 5;
                    }
                }
                PosDeliverServer posDeliverServer = new PosDeliverServer();
                Position ap = posDeliverServer.deliverAnchorPos(lng, lat, direction, did);
                log.info("rdx: " + rdx + " rdy:" + rdy);
                pos.setLongitude(lng);
                pos.setLatitude(lat);
                pos.setDirection(direction);
                pos.setAlongitude(ap.getLongitude());
                pos.setAlatitude(ap.getLatitude());
                pos.setAnchorID(CarTempData.carAnchorID.get(did));
                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss E");
                pos.setTime(dtf.format(ldt));
                String sendData = JSONObject.toJSONString(pos);
                try {
                    WebSocketServer.sendInfo(sendData, "user1");
                    log.info("websocket 发送成功： " + sendData);
                    Thread.sleep(3000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
