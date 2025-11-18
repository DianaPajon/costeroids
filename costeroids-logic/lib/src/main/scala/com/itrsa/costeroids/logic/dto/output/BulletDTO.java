package com.itrsa.costeroids.logic.dto.output;

import java.io.Serializable;

public class BulletDTO implements Serializable {
    
    CoordinateDTO center;
    double rotation;
    String id;
    String playerId;

    public BulletDTO(CoordinateDTO center, double rotation, String id, String playerId) {
        this.center = center;
        this.rotation = rotation;
        this.id = id;
        this.playerId = playerId;
    }

    public BulletDTO() {
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

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
