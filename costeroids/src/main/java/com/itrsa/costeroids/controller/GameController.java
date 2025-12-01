package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import com.itrsa.costeroids.logic.engine.GameEngine;
import com.itrsa.costeroids.state.StatePublisher;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

@Singleton
public class GameController {


    @Value("${costeroids.tick-delay}")
    private  Integer tickDelay = 15;

    @Value("${costeroids.update-delay}")
    private  Integer updateDelay = 30;

    private final EventQueue eventQueue;
    private final GameEngine engine = new GameEngine();
    private final StatePublisher statePublisher;

    public GameController(EventQueue eventQueue, StatePublisher statePublisher){
        this.eventQueue = eventQueue;
        this.statePublisher = statePublisher;
    }

    Long lastTick = 0L;

    private final LinkedList<EventDTO> processing = new LinkedList<>();


    private static int ticks = 0;

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

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

}
