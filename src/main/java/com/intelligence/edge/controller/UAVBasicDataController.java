package com.intelligence.edge.controller;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.UAVBasicData;
import com.intelligence.edge.service.CarBasicDataService;
import com.intelligence.edge.service.UAVBasicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device/UAV")
@Slf4j(topic = "c.UAVBasicDataController")
public class UAVBasicDataController {
    @Autowired
    UAVBasicDataService uavBasicDataService;

    /**
     *
     * @return 获取所有无人机的基本信息
     */
    @RequestMapping(value = "allUAVData")
    public List<UAVBasicData> getAllUAVBasicData() {
        return uavBasicDataService.getAllUAVBasicData();
    }

    /**
     * 根据设备id获取对应无人机基本信息
     * @param deviceID
     * @return
     */
    @RequestMapping(value = "getUAVData")
    public UAVBasicData getUAVBasicDataByID(String deviceID) {
        return uavBasicDataService.getUAVBasicDataByID(deviceID);
    }

    /**
     * 新增无人机
     * @param uav
     * @return
     */
    @PostMapping(value = "insertUAV")
    public int insertUAVBasicData(@RequestBody UAVBasicData uav) {
        log.info("新增UAV:"+uav);
        if(uav.getDeviceID()==null){
            return 0;
        }
        return uavBasicDataService.insertUAVBasicData(uav);
    }

    // 删除无人机
    @RequestMapping(value = "deleteUAV")
    public int deleteUAVBasicDataByID(String deviceID) {
        return uavBasicDataService.deleteUAVBasicDataByID(deviceID);
    }

    // 更新无人机信息
    @PostMapping(value = "updateUAV")
    public int updateCarBasicData(@RequestBody UAVBasicData uavBasicData) {
        log.info("UAVBasicData:"+uavBasicData);
        return uavBasicDataService.updateUAVBasicData(uavBasicData);
    }

}