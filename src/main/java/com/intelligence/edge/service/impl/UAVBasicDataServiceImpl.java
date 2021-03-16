package com.intelligence.edge.service.impl;

import com.intelligence.edge.dao.UAVBasicDataMapper;
import com.intelligence.edge.pojo.UAVBasicData;
import com.intelligence.edge.service.UAVBasicDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "c.UAVBasicDataServiceImpl")
public class UAVBasicDataServiceImpl implements UAVBasicDataService {

    @Autowired
    private UAVBasicDataMapper uavBasicDataMapper;

    /**
     * 查询无人机的所有设备信息
     * @return List<UAVBasicData>
     */
    public List<UAVBasicData> getAllUAVBasicData() {
        List<UAVBasicData> uavList = uavBasicDataMapper.getAllUAVBasicData();
        return uavList;
    }

    /**
     * 通过设备ID来查询无人机信息
     * @param deviceID
     * @return List<UAVBasicData>
     */
    public UAVBasicData getUAVBasicDataByID(String deviceID) {
        UAVBasicData uavBasicData = uavBasicDataMapper.getUAVBasicDataByID(deviceID);
        return uavBasicData;
    }

    /**
     * 添加设备信息
     * @param uav
     * @return int
     */
    public int insertUAVBasicData(UAVBasicData uav) {
        int res = 0;
        try{
            uav.setElectricity(100);
            uav.setState(0);
            res = uavBasicDataMapper.insertUAVBasicData(uav);
        }catch (Exception e){
        }
        return res;
    }

    /**
     * 删除设备信息
     */
    public int deleteUAVBasicDataByID(String deviceID) {
        int res = 0;
        try{
            res = uavBasicDataMapper.deleteUAVBasicDataByID(deviceID);
        }catch (Exception e){
        }
        return res;
    }

    /**
     * 更新设备信息
     */
    public int updateUAVBasicData(UAVBasicData uavBasicData) {
        return uavBasicDataMapper.updateUAVBasicData(uavBasicData);
    };


}
