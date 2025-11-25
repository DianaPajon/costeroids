package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.KeyDTO;
import com.itrsa.costeroids.logic.engine.GameEngine;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Function;

@Singleton
public class EngineController {

    final GameEngine engine = new GameEngine();
    Timer timer;
    Long lastTick = 0L;

    StatePublisher statePublisher = new StatePublisher();

    private LinkedTransferQueue<KeyDTO> arrivedEvents = new LinkedTransferQueue<KeyDTO>();
    private LinkedTransferQueue<KeyDTO> processing = new LinkedTransferQueue<KeyDTO>();

    private final ConcurrentMap<String, WebSocketSession> inputSocketMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StateSuscriber> suscribers = new ConcurrentHashMap<>();

    private static int ticks = 0;

    private static final Logger LOG = LoggerFactory.getLogger(EngineController.class);

    public EngineController(){
        this.timer = new Timer();
        lastTick = System.currentTimeMillis();
        timer.scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    ticks += TICK_RATE;
                    arrivedEvents.drainTo(processing);
                    processing.stream().forEach(
                            (event) -> engine.processEvents(event)
                    );
                    processing.clear();
                    if(ticks >= TICKS_UPDATE){
                        ticks = 0;
                        var newTick = System.currentTimeMillis();
                        engine.tick((double) (newTick - lastTick) / 1000);
                        var newState = engine.poll();
                        statePublisher.updateState(newState);
                        lastTick = newTick;
                    }
                }
            }
            ,TICK_RATE
            ,TICK_RATE
        );
    }

    public void updateState(KeyDTO keysPresses){
        arrivedEvents.offer(keysPresses);
    }

    public String addPlayer(String username, WebSocketSession session){
        var id = this.engine.addPlayer();
        var subscriber = new StateSuscriber(session);

        this.inputSocketMap.put(id, session);
        this.suscribers.put(username, subscriber);
        this.statePublisher.subscribe(subscriber);


        return id;
    }

    public void closeSession(String username){

        this.inputSocketMap.remove(username);

        var subscriber = this.suscribers.remove(username);
        subscriber.onComplete();

    }

    private static final Integer TICK_RATE = 10;
    private static final Integer TICKS_UPDATE = 30;
}
