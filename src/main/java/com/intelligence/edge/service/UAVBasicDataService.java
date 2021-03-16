package com.intelligence.edge.service;

import java.util.List;
import com.intelligence.edge.pojo.UAVBasicData;

public interface UAVBasicDataService {
    /**
     * 查询无人机的所有设备信息
     * @return List<UAVBasicData>
     */
    List<UAVBasicData> getAllUAVBasicData();

    /**
     * 通过设备ID来查询无人机信息
     * @param deviceID
     * @return List<UAVBasicData>
     */
    UAVBasicData getUAVBasicDataByID(String deviceID);

    /**
     * 添加设备信息
     * @param uavBasicData
     * @return int
     */
    int insertUAVBasicData(UAVBasicData uavBasicData);

    /**
     * 删除设备信息
     */
    int deleteUAVBasicDataByID(String deviceID);

    /**
     * 更新设备信息
     */
    int updateUAVBasicData(UAVBasicData uavBasicData);

}
