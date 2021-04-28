package com.intelligence.edge.controller;

import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import com.intelligence.edge.service.AnchorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/anchor")
@Slf4j(topic = "c.AnchorDataController")
public class AnchorDataController {

    @Autowired
    AnchorDataService anchorDataService;

    @RequestMapping(value = "allAnchorData")
    public List<AnchorInfo> getAllAnchorData(){
        return anchorDataService.getAllAnchorData();
    }

    @RequestMapping(value = "getAnchorData")
    public AnchorInfo getAnchorDataById(String anchorID){
        return anchorDataService.getAnchorDataById(anchorID);
    }

    @RequestMapping(value = "getAdjacentData")
    public AdjacentInfo getAdjacentDataById(String anchorID){
        return anchorDataService.getAdjacentById(anchorID);
    }
}
