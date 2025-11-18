package com.itrsa.costeroids.logic.dto.output;

import java.io.Serializable;

public class CoordinateDTO implements Serializable {
    double x;
    double y;

    public CoordinateDTO(double x, double y){
        this.x = x;
        this.y = y;
    }

    public CoordinateDTO() {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
