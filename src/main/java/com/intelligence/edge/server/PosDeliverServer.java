package com.intelligence.edge.server;

import com.intelligence.edge.dao.UWBAnchorDataMapper;
import com.intelligence.edge.data.CarTempData;
import com.intelligence.edge.pojo.AdjacentInfo;
import com.intelligence.edge.pojo.AnchorInfo;
import com.intelligence.edge.pojo.Position;
import com.intelligence.edge.utils.SpringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.applet.AppletContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 * 分发UWB基站坐标
 */

@Data
@Slf4j(topic = "c.PosDeliverServer")
public class PosDeliverServer {
    private ArrayList<AnchorInfo> anchorList;
    private ApplicationContext applicationContext = SpringUtils.getApplicationContext();
    private UWBAnchorDataMapper uadMapper =applicationContext.getBean(UWBAnchorDataMapper.class);
    private Double LSB_M_TO_LAT_LONG = 0.000008993216059;

    /*public Double triangleArea(Double x1, Double y1, Double x2, Double y2, Double x3, Double y3){
        Double result = Math.abs(x1 * y2 - x1 * y3 + x2 * y3 - x2 * y1 + x3 * y1 - x3 * y2)/2.0;
        return result;
    }

    public boolean PosIsInTriangle(Double longtitude, Double latitude, AnchorInfo pA, AnchorInfo pB, AnchorInfo pC){
        Double AreaO = triangleArea(pA.getLongtitude(), pA.getLatitude(), pB.getLongtitude(), pB.getLatitude(), pC.getLongtitude(), pC.getLatitude());
        Double AreaA = triangleArea(longtitude, latitude, pB.getLongtitude(), pB.getLatitude(), pC.getLongtitude(), pC.getLatitude());
        Double AreaB = triangleArea(pA.getLongtitude(), pA.getLatitude(), longtitude, latitude, pC.getLongtitude(), pC.getLatitude());
        Double AreaC = triangleArea(pA.getLongtitude(), pA.getLatitude(), pB.getLongtitude(), pB.getLatitude(), longtitude, latitude);
        AreaA = AreaA + AreaB + AreaC;
        Double epsilon = 0.00001;
        if(Math.abs(AreaO - AreaA) < epsilon)
            return true;
        return false;
    }*/

    //判断小车是否需要更换基站
    public boolean IsChangeCircle(Double longitude, Double latitude, String deviceID){
        String aID = CarTempData.carAnchorID.get(deviceID);
        if (CarTempData.anchorPos.get(aID) == null) {
            AnchorInfo anchorInfo = uadMapper.getAnchorInfoById(aID);
            CarTempData.anchorPos.put(aID, anchorInfo);
        }
        Double lng = Math.abs(longitude - CarTempData.anchorPos.get(aID).getLongitude());
        Double lat = Math.abs(latitude - CarTempData.anchorPos.get(aID).getLatitude());
        Double radius = CarTempData.anchorPos.get(aID).getRadius();
        if ((Math.pow(lng / LSB_M_TO_LAT_LONG, 2) + Math.pow(lat / LSB_M_TO_LAT_LONG, 2)) / Math.pow(radius, 2) >= 0.81) {
            return true;
        }
        return false;
    }

    //小车到某个基站的距离
    public Double disC2A(String anchorID, Double longitude, Double latitude) {
        Double a = Math.abs(longitude - CarTempData.anchorPos.get(anchorID).getLongitude());
        Double b = Math.abs(latitude - CarTempData.anchorPos.get(anchorID).getLatitude());
        Double dis = Math.sqrt(Math.pow(a / LSB_M_TO_LAT_LONG, 2) + Math.pow(b / LSB_M_TO_LAT_LONG, 2));
        log.info("anchorID:" + anchorID + "  dis:" + dis);
        return dis;
    }

    //给小车分发基站坐标
    public Position deliverAnchorPos(Double longitude, Double latitude, Double direction, String deviceID){
        Position ap = null;
        String anchorID = CarTempData.carAnchorID.get(deviceID);
        if (CarTempData.anchorPos.get(deviceID) == null) {
            AnchorInfo anchorInfo = uadMapper.getAnchorInfoById(anchorID);
            ap = new Position(anchorInfo.getLongitude(), anchorInfo.getLatitude());
            CarTempData.anchorPos.put(anchorID, anchorInfo);
        }
        if (IsChangeCircle(longitude, latitude, deviceID)) {
            AdjacentInfo adji;
            if (CarTempData.anchorAdj.get(anchorID) == null) {
                adji = uadMapper.getAnchorAdjacent(anchorID);
                CarTempData.anchorAdj.put(anchorID, adji);
            }else {
                adji = CarTempData.anchorAdj.get(anchorID);
            }
            ArrayList<String> adjList = new ArrayList<>();
            ArrayList<Double> angleList = new ArrayList<>();
            ArrayList<Double> disList = new ArrayList<>();
            adjList.add(anchorID);
            log.info("id: " + adjList.get(0));
            if (adji.getCenterA() != null) {
                if (CarTempData.anchorPos.get(adji.getCenterA()) == null) {
                    AnchorInfo aii = uadMapper.getAnchorInfoById(adji.getCenterA());
                    CarTempData.anchorPos.put(adji.getCenterA(), aii);
                }
                if (disC2A(adji.getCenterA(), longitude, latitude) < CarTempData.anchorPos.get(adji.getCenterA()).getRadius()) {
                    adjList.add(adji.getCenterA());
                }
            }
            if (adji.getCenterB() != null) {
                if (CarTempData.anchorPos.get(adji.getCenterB()) == null) {
                    AnchorInfo aii = uadMapper.getAnchorInfoById(adji.getCenterB());
                    CarTempData.anchorPos.put(adji.getCenterB(), aii);
                }
                if (disC2A(adji.getCenterB(), longitude, latitude) < CarTempData.anchorPos.get(adji.getCenterB()).getRadius()) {
                    adjList.add(adji.getCenterB());
                }
            }
            if (adji.getCenterC() != null) {
                if (CarTempData.anchorPos.get(adji.getCenterC()) == null) {
                    AnchorInfo aii = uadMapper.getAnchorInfoById(adji.getCenterC());
                    CarTempData.anchorPos.put(adji.getCenterC(), aii);
                }
                if (disC2A(adji.getCenterC(), longitude, latitude) < CarTempData.anchorPos.get(adji.getCenterC()).getRadius()) {
                    adjList.add(adji.getCenterC());
                }
            }
            if (adji.getCenterD() != null) {
                if (CarTempData.anchorPos.get(adji.getCenterD()) == null) {
                    AnchorInfo aii = uadMapper.getAnchorInfoById(adji.getCenterD());
                    CarTempData.anchorPos.put(adji.getCenterD(), aii);
                }
                if (disC2A(adji.getCenterD(), longitude, latitude) < CarTempData.anchorPos.get(adji.getCenterD()).getRadius()) {
                    adjList.add(adji.getCenterD());
                }
            }
            if (adji.getCenterE() != null) {
                if (CarTempData.anchorPos.get(adji.getCenterE()) == null) {
                    AnchorInfo aii = uadMapper.getAnchorInfoById(adji.getCenterE());
                    CarTempData.anchorPos.put(adji.getCenterE(), aii);
                }
                if (disC2A(adji.getCenterE(), longitude, latitude) < CarTempData.anchorPos.get(adji.getCenterE()).getRadius()) {
                    adjList.add(adji.getCenterE());
                }
            }
            if (adji.getCenterF() != null) {
                if (CarTempData.anchorPos.get(adji.getCenterF()) == null) {
                    AnchorInfo aii = uadMapper.getAnchorInfoById(adji.getCenterF());
                    CarTempData.anchorPos.put(adji.getCenterF(), aii);
                }
                if (disC2A(adji.getCenterF(), longitude, latitude) < CarTempData.anchorPos.get(adji.getCenterF()).getRadius()) {
                    adjList.add(adji.getCenterF());
                }
            }
            int goal = 0;
            Double carAngle = Math.PI * direction / 180;
            if (adjList.size() > 1) {
                for (int i = 0; i < adjList.size(); i++) {
                    Double dot = CarTempData.anchorPos.get(adjList.get(i)).getLatitude() - latitude;
                    Double diss = Math.sqrt(Math.pow(CarTempData.anchorPos.get(adjList.get(i)).getLongitude() - longitude, 2) +
                            Math.pow(CarTempData.anchorPos.get(adjList.get(i)).getLatitude() - latitude, 2));
                    Double angle = Math.acos(dot / diss);
                    if (CarTempData.anchorPos.get(adjList.get(i)).getLongitude() < longitude) {
                        angle = 2 * Math.PI - angle;
                    }
                    Double abs = Math.abs(carAngle - angle);
                    angleList.add(Math.min(abs, Math.PI * 2 - abs));
                    log.info("adj:" + adjList.get(i) + " angle:" + angleList.get(i));
                }
                Double min = angleList.get(0);
                for (int j = 1; j < angleList.size(); j++) {
                    if(angleList.get(j)<min){
                        min = angleList.get(j);
                        goal = j;
                    }
                }
            }
            log.info("new_anchor_notsure:" + adjList.get(goal));
            Double lng = Math.abs(longitude - CarTempData.anchorPos.get(adjList.get(goal)).getLongitude());
            Double lat = Math.abs(latitude - CarTempData.anchorPos.get(adjList.get(goal)).getLatitude());
            Double radius = CarTempData.anchorPos.get(adjList.get(goal)).getRadius();
            if ((Math.pow(lng / LSB_M_TO_LAT_LONG, 2) + Math.pow(lat / LSB_M_TO_LAT_LONG, 2)) / Math.pow(radius, 2) >= Math.pow(0.95, 2)) {
                for (int t = 0; t < adjList.size(); t++) {
                    Double dis = disC2A(adjList.get(t), longitude, latitude);
                    disList.add(dis);
                }
                Double mindis = disList.get(0);
                goal = 0;
                for (int m = 1; m < disList.size(); m++) {
                    if (mindis > disList.get(m)) {
                        mindis = disList.get(m);
                        goal = m;
                    }
                }
            }
            ap.setLongitude(CarTempData.anchorPos.get(adjList.get(goal)).getLongitude());
            ap.setLatitude(CarTempData.anchorPos.get(adjList.get(goal)).getLatitude());
            CarTempData.carAnchorID.put(deviceID, adjList.get(goal));
        }
        log.info("now_anchor:" + CarTempData.carAnchorID.get(deviceID));
        return ap;
    }

    /*public Position deliverPos2D(Double dis0, Double dis1, Double dis2){

    }*/

}
