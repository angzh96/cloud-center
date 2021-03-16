package com.intelligence.edge.controller;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.server.CarControlServer;
import com.intelligence.edge.service.CarNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author shik2
 */
@RestController
@CrossOrigin
@RequestMapping("/data/car")
@Slf4j(topic = "c.CarNetController")
public class CarNetController {

    @Autowired
    private CarNetService carServer;

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    @Autowired
    private CarConfig carConfig;

    /**
     * 测试新添加设备是否可以连接
     *
     * @param IP
     * @return
     */
    @RequestMapping("/testConnect")
    public int testConnect(@RequestParam("IP") String IP) {
        log.info("设备ip:" + IP);
        return carServer.ping(IP) ? 1 : 0;
    }


    /**
     * 已无用
     * 传入设备id，开启对应无人车环境数据和视频数据的udp接收端,修改对应车
     *
     * @param deviceID
     * @return
     * @throws IOException
     */
   // @RequestMapping("/connect")
   // public int connect(@RequestParam("deviceID") String deviceID) {
        // 设备无法连通
        /*String carIP = CarTempData.carIP.get(carID);
        if (!carServer.ping(carIP)) {
            log.info("设备离线：" + carID);
            return 0;
        }*/
        /*if(CarTempData.carState.get(deviceID) == 0){
            log.info("设备离线：" + deviceID);
            return 0;
        }
        return carServer.connect(deviceID);*/
    //}

    /**
     * 关闭对应id设备的数据接收端
     *
     * @param deviceID
     * @return
     */
    @RequestMapping("/closeConnect")
    public int closeConnect(@RequestParam("deviceID") String deviceID) {
        // 设备无法连通
        if(CarTempData.carState.get(deviceID) == 0){
            log.info("设备离线：" + deviceID);
            return 0;
        }
        return carServer.closeConnect(deviceID);
        //CarBasicData car=carBasicDataMapper.getCarBasicDataByID(deviceID);
        /*CarControlServer carControlServer = new CarControlServer(deviceID, cPort);
        carControlServer.reset();
        return 1;*/
    }
}
