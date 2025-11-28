package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import com.itrsa.costeroids.logic.dto.input.EventType;
import com.itrsa.costeroids.logic.engine.GameEngine;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedTransferQueue;

@Singleton
public class EngineController {

    final GameEngine engine = new GameEngine();
    Timer timer;
    Long lastTick = 0L;

    StatePublisher statePublisher = new StatePublisher();

    private LinkedTransferQueue<EventDTO> arrivedEvents = new LinkedTransferQueue<EventDTO>();
    private LinkedList<EventDTO> processing = new LinkedList<EventDTO>();

    private final ConcurrentMap<String, WebSocketSession> inputSocketMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StateSuscriber> suscribers = new ConcurrentHashMap<>();

    private static int ticks = 0;

    private static final Logger LOG = LoggerFactory.getLogger(EngineController.class);

    public EngineController(){

    }

    @Scheduled(fixedDelay = "15ms")
    public void engineTick(){
        ticks += TICK_RATE;
        arrivedEvents.drainTo(processing);
        engine.processEvents(processing);
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
    public void updateState(EventDTO keysPresses){
        arrivedEvents.offer(keysPresses);
    }

    public String addPlayer(String username, WebSocketSession session){
        var id = UUID.randomUUID().toString();
        var eventDTO = new EventDTO(EventType.NEW_PLAYER_EVENT, id);
        arrivedEvents.offer(eventDTO);
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

    private static final Integer TICK_RATE = 15;
    private static final Integer TICKS_UPDATE = 30;
}
