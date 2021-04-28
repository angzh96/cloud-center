package com.intelligence.edge.service.impl;

import com.intelligence.edge.dao.UWBAnchorDataMapper;
import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import com.intelligence.edge.service.AnchorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "c.AnchorDataServiceImpl")
public class AnchorDataServiceImpl implements AnchorDataService {
    @Autowired
    UWBAnchorDataMapper uwbAnchorDataMapper;

    public List<AnchorInfo> getAllAnchorData() {
        return uwbAnchorDataMapper.getAllAnchorInfo();
    }

    public AnchorInfo getAnchorDataById(String anchorID) {
        return uwbAnchorDataMapper.getAnchorInfoById(anchorID);
    }

    public AdjacentInfo getAdjacentById(String anchorID) {
        return uwbAnchorDataMapper.getAnchorAdjacent(anchorID);
    }
}
