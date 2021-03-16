package com.intelligence.edge.service.impl;

import com.intelligence.edge.dao.CarBasicDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.CarBasicData;
import com.intelligence.edge.pojo.Position;
import com.intelligence.edge.server.CarControlServer;
import com.intelligence.edge.service.CarBasicDataService;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "c.CarBasicDataServiceImpl")
public class CarBasicDataServiceImpl implements CarBasicDataService {

    @Autowired
    private CarBasicDataMapper carBasicDataMapper;

    /**
     * 查询所有智能车信息
     * @return
     */
    @Override
    public List<CarBasicData> getAllCarBasicData() {
        List<CarBasicData> carList = carBasicDataMapper.getAllCarBasicData();
        for (CarBasicData car : carList) {
            car.setLongitude(CarTempData.carPos.get(car.getDeviceID()).getLongitude());
            car.setLatitude(CarTempData.carPos.get(car.getDeviceID()).getLatitude());
            if(CarTempData.carState.get(car.getDeviceID()) == 1){
                log.info("state:1");
                car.setState(1);
            }else{
                log.info("state:0");
                car.setState(0);
            }
        }
        return carList;
    }

    /**
     * 查询单辆车信息
     * @param deviceID
     * @return
     */
    @Override
    public CarBasicData getCarBasicDataByID(String deviceID) {
        CarBasicData car = carBasicDataMapper.getCarBasicDataByID(deviceID);
        car.setLongitude(CarTempData.carPos.get(car.getDeviceID()).getLongitude());
        car.setLatitude(CarTempData.carPos.get(car.getDeviceID()).getLatitude());
        if(CarTempData.carState.get(car.getDeviceID()) == 1){
            car.setState(1);
        }else{
            car.setState(0);
        }
        return car;
    }

    /**
     * 新增车辆
     * @param carBasicData
     * @return
     */
    @Override
    public int insertCarBasicData(CarBasicData carBasicData) {
        int res = 0;
        try {
            carBasicData.setElectricity(100);
            carBasicData.setState(0);
            res = carBasicDataMapper.insertCarBasicData(carBasicData);
            if(res==1){
                CarTempData.carState.put(carBasicData.getDeviceID(), 0);
                Position position = new Position(0.0, 0.0);
                CarTempData.carPos.put(carBasicData.getDeviceID(),position);
                // 开启新增车辆的监听
                CarControlServer server = new CarControlServer(carBasicData.getDeviceID(),carBasicData.getcPort());
                server.openConnect();
                // 移动控制服务端存入CarTempData
                CarTempData.ccsMap.put(server.getDeviceID(),server);
            }
        }catch (Exception e){
        }
        return res;
    }

    /**
     * 删除车辆
     * @param carID
     * @return
     */
    @Override
    public int deleteCarBasicDataByID(String carID) {
        int res = 0;
        try {
            res = carBasicDataMapper.deleteCarBasicDataByID(carID);
            // 关掉对应的车子的控制服务端，并删除CarTempData中的相关信息
            CarTempData.ccsMap.get(carID).close();
            if(res==1){
                CarTempData.carPos.remove(carID);
                synchronized (this){
                    for (CarBasicData carBasicData : CarTempData.carList) {
                        if(carBasicData.getDeviceID().equals(carID))
                            CarTempData.carList.remove(carBasicData);
                    }
                }
                CarTempData.ccsMap.remove(carID);
                CarTempData.carState.remove(carID);
                CarTempData.carControlPort.remove(carID);
                CarTempData.carENVPort.remove(carID);
                CarTempData.carVideoPort.remove(carID);
            }
        }catch (Exception e){
        }
        return res;
    }

    /**
     * 更新车辆信息
     * @param carBasicData
     * @return
     */
    public int updateCarBasicData(CarBasicData carBasicData) {
        return carBasicDataMapper.updateCarBasicData(carBasicData);
    }

}
