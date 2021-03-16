package com.intelligence.edge.server;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shik2
 * @date 2020/07/02
 **/
@Data
@Slf4j(topic = "c.CarControlServer")
public class CarControlServer {
    private String deviceID;
    private int port;
    private ServerSocket server;
    private Socket socket;
    private Thread socketThread;
    private DataOutputStream out;
    private List<CarENVServer> cenvList = new ArrayList<>();
    private List<CarVideoServer> cvsList = new ArrayList<>();

    public CarControlServer(String deviceID, int port) {
        this.deviceID = deviceID;
        this.port = port;
    }

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    public void openConnect() throws IOException {
        server = new ServerSocket(port);//创建ServerSocket类
        socketThread = new Thread(new Task(socket));
        socketThread.start();
        log.info("开启控制服务端：" + deviceID + "-" + port);
    }

    class Task implements Runnable {

        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                socket = server.accept();// 等待客户连接
                out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());// 读取客户端传过来信息的DataInputStream
                String regex = "/^id=.*/";
                    String msg = in.readLine();// 读取来自客户端的信息
                    if(!deviceID.equals(msg)){
                        log.info("id与端口不匹配");
                        reset();
                    }else{
                        log.info(deviceID + " 控制服务端收到信息：" + msg);
                        CarTempData.carState.put(deviceID, 1);//表示设备已经在线
                        receiveData(deviceID);
                        receiveVideo(deviceID);
                        Thread threadP = new Thread(new Polling(deviceID));
                        threadP.start();
                        log.info(deviceID+"设备状态修改完毕，可开启数据连接");
                    }
            } catch (IOException e) {
            }

        }
    }

    public void control(String instruction) {
        log.info(deviceID+"发送指令：" + instruction);
        try {
            out.write(instruction.getBytes());//将客户端的信息传递给服务器
        } catch (IOException e) {
            log.info("发送指令失败，需要重新连接设备");
            reset();
        }
    }

    public void reset() {
        try {
            server.close();
            CarTempData.carState.put(deviceID, 0);
            openConnect();
            closeConnection(deviceID);
            closeVideo(deviceID);
            log.info("重置"+deviceID+"-"+port+"控制服务端");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("关闭异常！");
        }
    }

    public void close() {
        try {
            server.close();
            log.info("关闭"+deviceID+"控制服务端");
        } catch (IOException e) {
            log.info("关闭异常！");
        }
    }

    class Polling implements Runnable {

        private String deviceID;

        public Polling(String deviceID) { this.deviceID = deviceID; }

        @Override
        public void run() {
            try {
                while (true){
                    try{
                        Thread.sleep(60000);
                    }catch (InterruptedException e){
                        log.info(deviceID+"轮询线程中断");
                        e.printStackTrace();
                    }
                    log.info("向"+deviceID+"发送轮询指令：check");
                    out.write("check".getBytes());
                }
            }catch (IOException e){
                log.info(deviceID+"轮询异常");
                reset();
            }
        }
    }

    /**
     * 接收无人车的发送的数据报文
     *
     * @param carID
     */
    public void receiveData(String carID) {

        try {
            int flag = 0;
            CarENVServer useServer = null;
            for (CarENVServer cbs : cenvList) {
                if (cbs.getDeviceID().equals(carID)) {
                    useServer = cbs;
                    log.info("使用已有车辆数据连接");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                useServer = new CarENVServer(carID, CarTempData.carENVPort.get(carID));
                cenvList.add(useServer);
                log.info("创建新车辆数据连接");
            }
            useServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收无人车的发送的视频文件
     *
     * @param carID
     */
    public void receiveVideo(String carID) {

        try {
            int flag = 0;
            CarVideoServer useServer = null;
            for (CarVideoServer cvs : cvsList) {
                if (cvs.getDeviceID().equals(carID)) {
                    useServer = cvs;
                    log.info("使用已有车辆视频连接");
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                useServer = new CarVideoServer(carID, CarTempData.carVideoPort.get(carID));
                cvsList.add(useServer);
                log.info("创建新车辆视频连接");
            }
            useServer.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(String carID) {
        for (CarENVServer cbs : cenvList) {
            if (cbs.getDeviceID().equals(carID)) {
                cbs.end();
                log.info("关闭已有车辆数据连接");
                break;
            }
        }
    }

    public void closeVideo(String carID) {
        for (CarVideoServer cvs : cvsList) {
            if (cvs.getDeviceID().equals(carID)) {
                cvs.end();
                log.info("关闭已有车辆视频连接");
                break;
            }
        }
    }
}
