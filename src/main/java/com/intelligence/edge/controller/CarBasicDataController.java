package com.intelligence.edge.controller;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.service.CarBasicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/device/car")
@Slf4j(topic = "c.CarBasicDataController")
public class CarBasicDataController {
    @Autowired
    CarBasicDataService carBasicDataService;

    @RequestMapping(value = "test")
    public Integer hello() {

        return 1;
    }

    /**
     *
     * @return 获取所有无人车的基本信息
     */
    @RequestMapping(value = "allCarData")
    public List<CarBasicData> getAllCarBasicData() {
        return carBasicDataService.getAllCarBasicData();
    }

    /**
     * 根据设备id获取对应无人车基本信息
     * @param deviceID
     * @return
     */
    @RequestMapping(value = "getCarData")
    public CarBasicData getCarBasicDataByID(String deviceID) {
        return carBasicDataService.getCarBasicDataByID(deviceID);
    }

    /**
     * 新增无人车
     * @param car
     * @return
     */
    @PostMapping(value = "insertCar")
    public int insertCarBasicData(@RequestBody CarBasicData car) {
        log.info("新增car:"+car);
        if(car.getDeviceID()==null){
            return 0;
        }
        return carBasicDataService.insertCarBasicData(car);
    }

    // 删除无人车
    @RequestMapping(value = "deleteCar")
    public int deleteCarBasicDataByID(String deviceID) {
        return carBasicDataService.deleteCarBasicDataByID(deviceID);
    }

    // 更新无人车信息
    @PostMapping(value = "updateCar")
    public int updateCarBasicData(@RequestBody CarBasicData carBasicData) {
        log.info("carBasicData:"+carBasicData);
        return carBasicDataService.updateCarBasicData(carBasicData);
    }

}
