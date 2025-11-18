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
    private final ConcurrentMap<String, WebSocketSession> inputSocketMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StateSuscriber> suscribers = new ConcurrentHashMap<>();

    StatePublisher statePublisher = new StatePublisher();

    public GameController(EngineController engineController){
        this.engineController = engineController;
        this.engineController.setStatePublisher(statePublisher);
    }



    public String addPlayer(String username, WebSocketSession session){
        var id = this.engineController.addPlayer();
        var suscriber = new StateSuscriber(session);
        this.inputSocketMap.put(id, session);
        this.suscribers.put(username, suscriber);
        this.statePublisher.subscribe(suscriber);
        return id;
    }

    public void closeSession(String username){
        this.inputSocketMap.remove(username);
        var suscriber = this.suscribers.remove(username);
        suscriber.onComplete();
    }

}
