package com.intelligence.edge.pojo;


import lombok.Data;

/**
 * @Author cxw
 * @Date 2021/04/7  15:41
 * @Description 小车在UWB环境下的数据类
 */
@Data
public class CarUWBInfo {
    private String time;
    private String deviceID;
    private Double longitude;
    private Double latitude;
    private Double direction;
    private String anchorID;
    private Double alongitude;
    private Double alatitude;

    public CarUWBInfo(String time, String deviceID, Double longitude,
                      Double latitude, Double direction, Double alongitude, Double alatitude, String anchorID) {
        this.time = time;
        this.deviceID = deviceID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.direction = direction;
        this.alatitude = alatitude;
        this.alongitude = alongitude;
        this.anchorID = anchorID;
    }

}
