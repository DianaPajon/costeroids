package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import com.itrsa.costeroids.logic.dto.input.EventType;
import com.itrsa.costeroids.logic.engine.GameEngine;
import com.itrsa.costeroids.state.StatePublisher;
import com.itrsa.costeroids.state.StateSuscriber;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

@Singleton
public class EngineController {


    @Value("${costeroids.tick-delay}")
    private  Integer tickDelay = 15;

    @Value("${costeroids.update-delay}")
    private  Integer updateDelay = 30;

    private final EventQueue eventQueue;
    private final GameEngine engine = new GameEngine();

    public EngineController(EventQueue eventQueue){
        this.eventQueue = eventQueue;
    }

    Long lastTick = 0L;

    StatePublisher statePublisher = new StatePublisher();

    private final LinkedList<EventDTO> processing = new LinkedList<>();


    private static int ticks = 0;

    private static final Logger LOG = LoggerFactory.getLogger(EngineController.class);

    @Scheduled(fixedDelay = "${costeroids.tick-delay}ms")
    public void engineTick(){
        ticks += tickDelay;
        eventQueue.drainTo(processing);
        engine.processEvents(processing);
        processing.clear();
        if(ticks >= updateDelay){
            ticks = 0;
            var newTick = System.currentTimeMillis();
            engine.tick((double) (newTick - lastTick) / 1000);
            var newState = engine.poll();
            statePublisher.updateState(newState);
            lastTick = newTick;
        }
    }

    public String addPlayer(String username, WebSocketSession session){
        var id = UUID.randomUUID().toString();
        var eventDTO = new EventDTO(EventType.NEW_PLAYER_EVENT, id);
        eventQueue.offer(eventDTO);
        var subscriber = new StateSuscriber(session);
        this.statePublisher.subscribe(subscriber);
        return id;
    }

}
