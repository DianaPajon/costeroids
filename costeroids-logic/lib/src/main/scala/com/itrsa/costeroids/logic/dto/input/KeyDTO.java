package com.itrsa.costeroids.logic.dto.input;

import com.itrsa.costeroids.logic.events.*;

import java.io.Serializable;


public class KeyDTO implements Serializable {
    KeyType type;
    String playerId;

    public KeyDTO(KeyType type, String playerId){
        this.type = type;
    }
    public KeyDTO(){};

    public KeyType getType(){
        return type;
    }
    public void setKeyType(KeyType type){
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public KeyEvent getEvent() throws Exception{
        switch (this.type){
            case KEY_TRUST_EVENT -> {
                return new ThrustEvent(playerId);
            }
            case KEY_LEFT_EVENT -> {
                return new KeyLeftEvent(playerId);
            }
            case KEY_RIGHT_EVENT -> {
                return new KeyRightEvent(playerId);
            }
            case KEY_BRAKE_EVENT -> {
                return new KeyBrakeEvent(playerId);
            }
            case FIRE_EVENT -> {
                return new FireEvent(playerId);
            }
        }
        throw new Exception("Invalid event");
    }
}
