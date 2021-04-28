package com.intelligence.edge.dao;

import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UWBAnchorDataMapper {
    List<AnchorInfo> getAllAnchorInfo();

    AdjacentInfo getAnchorAdjacent(String anchorID);

    AnchorInfo getAnchorInfoById(String anchorID);
}
