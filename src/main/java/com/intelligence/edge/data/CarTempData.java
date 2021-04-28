package com.intelligence.edge.data;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.dao.UWBAnchorDataMapper;
import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.Position;
import com.intelligence.edge.server.CarControlServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * @author shik2
 * @date 2020/06/29
 **/
@Component
@Slf4j(topic = "c.CarTempData")
public class CarTempData {
    // 位置
    public static Map<String, Position> carPos = new HashMap<>();
    // 基本信息
    public static List<CarBasicData> carList;
    // 智能车的移动控制服务端
    public static Map<String, CarControlServer> ccsMap = new HashMap<>();
    // 连接状态
    public static Map<String, Integer> carState = new HashMap<>();

    // 无人车控制服务端map
    public static Map<String, Integer> carControlPort = new HashMap<>();
    // 无人车环境数据接收端口map
    public static Map<String, Integer> carENVPort = new HashMap<>();
    // 无人车视频接收端口map
    public static Map<String, Integer> carVideoPort = new HashMap<>();

    // 分配给无人车的基站ID
    public static Map<String, String> carAnchorID = new HashMap<>();
    // 无人车与基站的距离
    public static Map<String, LinkedList<Double> > carDistance = new HashMap<>();
    // 基站与所属系统信息的映射
    public static Map<String, AnchorInfo> anchorPos = new HashMap<>();
    // 无人车接收UWB环境数据端口map
    public static Map<String, Integer> carUWBPort = new HashMap<>();
    // 基站与临近基站的映射
    public static Map<String, AdjacentInfo> anchorAdj = new HashMap<>();
    public static List<AnchorInfo> anchorList;


    @Autowired
    private CarBasicDataMapper carBasicDataMapper;
    @Autowired
    private UWBAnchorDataMapper uwbAnchorDataMapper;



    /**
     * 初始化所有智能车的位置和连接状态
     */
    @PostConstruct
    public void init() {
        log.info("------读取数据库车辆基本信息------");
        //加载无人车设备,配置端口信息
        carList = carBasicDataMapper.getAllCarBasicData();
        for (CarBasicData car : carList) {
            System.out.println(car);
            Position position = new Position(120.35, 30.32);
            carPos.put(car.getDeviceID(), position);
            carState.put(car.getDeviceID(), 0);  // 设备初始化为离线
            // 数据接收端状态置为0
            CarBasicData param = new CarBasicData();
            param.setDeviceID(car.getDeviceID());
            carControlPort.put(car.getDeviceID(),car.getcPort());
            carENVPort.put(car.getDeviceID(),car.getePort());
            carVideoPort.put(car.getDeviceID(),car.getvPort());
        }
        carUWBPort.put("car1", 10001);
        log.info("------读取数据库基站基本信息------");
        anchorList = uwbAnchorDataMapper.getAllAnchorInfo();
        for (AnchorInfo ai : anchorList) {
            System.out.println(ai);
            CarTempData.anchorPos.put(ai.getAnchorID(), ai);
        }
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

}
