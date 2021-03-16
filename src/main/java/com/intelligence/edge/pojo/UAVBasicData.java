package com.intelligence.edge.pojo;


import sun.font.DelegatingShape;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author cxw
 * @Date  2020/10/17
 * @Description 无人机基本数据类
 */
public class UAVBasicData {
    @NotNull
    private String deviceID;
    private String type;
    private String owner;
    private Integer electricity;
    private Integer state;
    private Double longitude;
    private Double latitude;
    private Integer cPort;
    private Integer ePort;
    private Integer vPort;
    //高度
    private Double alt;
    //相对地面速度
    private Double groundspeed;
    //偏航角
    private Double yaw;
    //偏航角速度
    private Double yawspeed;
    //温度
    private Double temperature;
    //风速
    private Double wind;
    //湿度
    private Double humidity;
    //发送数据时间
    private Date time;

    public UAVBasicData(@NotNull String deviceID, String type, String owner, Integer cPort, Integer ePort, Integer vPort){
        this.deviceID = deviceID;
        this.type = type;
        this.owner = owner;
        this.cPort = cPort;;
        this.ePort = ePort;
        this.vPort = vPort;
    }

    public UAVBasicData() {
    }

    public String getDeviceID() { return deviceID; }
    public void setDeviceID(String deviceID) { this.deviceID = deviceID; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Integer getElectricity() { return electricity; }
    public void setElectricity(Integer electricity) { this.electricity = electricity; }
    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Integer getcPort() { return cPort; }
    public void setcPort(Integer cPort) { this.cPort = cPort; }
    public Integer getePort() { return ePort; }
    public void setePort(Integer ePort) { this.ePort = ePort; }
    public Integer getvPort() { return vPort; }
    public void setvPort(Integer vPort) { this.vPort = vPort; }
    public Double getAlt() { return alt; }
    public void setAlt(Double alt) { this.alt = alt; }
    public Double getGroundspeed() { return groundspeed; }
    public void setGroundspeed(Double groundspeed) { this.groundspeed = groundspeed; }
    public Double getYaw() { return yaw; }
    public void setYaw(Double yaw) { this.yaw = yaw; }
    public Double getYawspeed() { return yawspeed; }
    public void setYawspeed(Double yawspeed) { this.yawspeed = yawspeed; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getWind() { return wind; }
    public void setWind(Double wind) { this.wind = wind; }
    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }
    public Date getTime() { return time;}
    public void setTime(Date time) { this.time = time; }

    @Override
    public String toString() {
        return "UAVBasicData{" +
                "deviceID='" + deviceID + '\'' +
                ", type='" + type + '\'' +
                ", owner='" + owner + '\'' +
                ", electricity=" + electricity +
                ", state=" + state +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", cPort=" + cPort +
                ", ePort=" + ePort +
                ", vPort=" + vPort +
                ", alt=" + alt +
                ", groundspeed=" + groundspeed +
                ", yaw=" + yaw +
                ", yawspeed=" + yawspeed +
                ", temperature=" + temperature +
                ", wind=" + wind +
                ", humidity=" + humidity +
                ", time=" + time +
                '}';
    }
}
