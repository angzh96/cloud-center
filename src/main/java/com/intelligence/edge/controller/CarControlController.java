package com.intelligence.edge.controller;

import com.intelligence.edge.config.CarConfig;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.service.CarControlService;
import com.intelligence.edge.service.CarNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shik2
 * @date 2020/07/02
 **/
@RestController
@CrossOrigin
@RequestMapping("/control/car")
@Slf4j(topic = "c.CarNetController")
public class CarControlController {
    @Autowired
    private CarControlService carControlService;

    @Autowired
    private CarNetService carServer;

    @Autowired
    private CarConfig carConfig;

    /**
     * 重启单台智能车的控制服务端
     * @param deviceID
     * @return
     */
    @RequestMapping("/reset")
    public int reset(@RequestParam("deviceID") String deviceID) {
        carControlService.reset(deviceID);
        log.info("设备控制端口重启成功：" + deviceID);
        return 1;
    }


    /**
     * 向对应智能车发送移动指令
     * @param deviceID
     * @param instruction
     * @return
     */
    @RequestMapping("/send")
    public int control(@RequestParam("deviceID") String deviceID,@RequestParam("instruction") String instruction) {
        if(CarTempData.carState.get(deviceID)==0){
            log.info("设备离线：" + deviceID);
            return 0;
        }
        carControlService.control(deviceID,instruction);
        return 1;
    }
}