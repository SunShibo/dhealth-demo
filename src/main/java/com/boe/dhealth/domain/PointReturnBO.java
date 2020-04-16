package com.boe.dhealth.domain;

public class PointReturnBO {

    private String x;
    private String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public PointReturnBO(String x, String y) {
        this.x = x;
        this.y = y;
    }


    public PointReturnBO() {

    }

    public PointReturnBO(String str) {
        String[] split = str.split(":");
        this.x=split[0];
        this.y=split[1];
    }
}
