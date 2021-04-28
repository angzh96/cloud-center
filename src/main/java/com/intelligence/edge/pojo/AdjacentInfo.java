package com.intelligence.edge.pojo;


import lombok.Data;

@Data
public class AdjacentInfo {
    private String id;
    private String centerA;
    private String centerB;
    private String centerC;
    private String centerD;
    private String centerE;
    private String centerF;

    public AdjacentInfo(String id, String centerA, String centerB, String centerC, String centerD, String centerE, String centerF){
        this.id = id;
        this.centerA = centerA;
        this.centerB = centerB;
        this.centerC = centerC;
        this.centerD = centerD;
        this.centerE = centerE;
        this.centerF = centerF;
    }
}
