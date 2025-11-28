package com.itrsa.costeroids.logic.dto.input;

import com.itrsa.costeroids.logic.events.*;

import java.io.Serializable;
import java.util.UUID;


public class EventDTO implements Serializable {
    EventType type;
    String playerId;

    public EventDTO(EventType type, String playerId){
        this.type = type;
        this.playerId = playerId;
    }
    public EventDTO(){};

    public EventType getType(){
        return type;
    }
    public void setKeyType(EventType type){
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public GameEvent getEvent() throws Exception{
        switch (this.type){
            case KEY_TRUST_EVENT -> {
                return new PlayerEvent(new  ThrustEvent(playerId));
            }
            case KEY_LEFT_EVENT -> {
                return new PlayerEvent(new  KeyLeftEvent(playerId));
            }
            case KEY_RIGHT_EVENT -> {
                return new PlayerEvent(new  KeyRightEvent(playerId));
            }
            case KEY_BRAKE_EVENT -> {
                return new PlayerEvent(new  KeyBrakeEvent(playerId));
            }
            case FIRE_EVENT -> {
                return new PlayerEvent(new FireEvent(playerId));
            }
            case NEW_PLAYER_EVENT -> {
                return new NewPlayerEvent(playerId);
            }
        }
        throw new Exception("Invalid event");
    }
}
