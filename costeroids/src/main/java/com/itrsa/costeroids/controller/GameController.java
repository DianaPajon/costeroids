package com.itrsa.costeroids.controller;

import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class GameController {

    private final EngineController engineController;

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    public GameController(EngineController engineController){
        this.engineController = engineController;
    }



    public String addPlayer(String username, WebSocketSession session){
        var id = this.engineController.addPlayer(username, session);
        return id;
    }

    public void closeSession(String username){
        this.engineController.closeSession(username);
    }

}
