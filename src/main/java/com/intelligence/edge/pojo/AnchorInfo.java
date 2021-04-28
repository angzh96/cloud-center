package com.intelligence.edge.pojo;

import lombok.Data;

@Data
public class AnchorInfo {
    private String anchorID;
    private Double longitude;
    private Double latitude;
    private Double radius;

    public AnchorInfo(String anchorID, Double alongitude, Double alatitude,
                      Double radius) {
        this.anchorID = anchorID;
        this.longitude = alongitude;
        this.latitude = alatitude;
        this.radius = radius;
    }
}
