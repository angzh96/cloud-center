package com.intelligence.edge.service;

import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import sun.plugin.javascript.navig.Anchor;

import java.util.List;

public interface AnchorDataService {

    /**
     * 查询所有基站信息
     * @return List<AnchorInfo>
     */
    List<AnchorInfo> getAllAnchorData();

    /**
     * 根据ID查询基站信息
     * @param anchorID
     * @return AnchorInfo
     */
    AnchorInfo getAnchorDataById(String anchorID);

    /**
     * 根据ID查询基站的临近基站信息
     * @param anchorID
     * @return AdjacentInfo
     */
    AdjacentInfo getAdjacentById(String anchorID);
}
