package com.itrsa.costeroids.logic.dto.output;

import java.io.Serializable;

public class ShipDTO implements Serializable {
    CoordinateDTO center;
    double rotation;
    String id;
    Integer hp;
    

    public ShipDTO(CoordinateDTO center, double rotation, String id, Integer hp) {
        this.center = center;
        this.rotation = rotation;
        this.id = id;
        this.hp = hp;
    }

    public ShipDTO() {
    }

    public CoordinateDTO getCenter() {
        return center;
    }

    public void setCenter(CoordinateDTO center) {
        this.center = center;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }
}
