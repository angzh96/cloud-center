package com.intelligence.edge.dao;

import com.intelligence.edge.pojo.UAVBasicData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UAVBasicDataMapper {

    List<UAVBasicData> getAllUAVBasicData();

    UAVBasicData getUAVBasicDataByID(String UAVID);

    int insertUAVBasicData(UAVBasicData uavBasicData);

    int deleteUAVBasicDataByID(String UAVID);

    int updateUAVBasicData(UAVBasicData uavBasicData);

}
