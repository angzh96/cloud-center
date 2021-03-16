package com.intelligence.edge.pojo;

import lombok.Data;

/**
 * @Author shik2
 * @Date 2020/06/19 15:18
 * @Description 环境信息POJO类
 **/
@Data
public class EnvironmentInfo {
    private String time;
    private String deviceID;
    private Double longitude;
    private Double latitude;
    private Double temperature;
    private Double wind;
    private Double humidity;

    public EnvironmentInfo(String time, String deviceID, Double longitude, Double latitude, Double temperature, Double wind, Double humidity) {
        this.time = time;
        this.deviceID = deviceID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
    }
}
