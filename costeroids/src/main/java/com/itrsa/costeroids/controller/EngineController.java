package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.KeyDTO;
import com.itrsa.costeroids.logic.engine.GameEngine;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class EngineController {

    final GameEngine engine = new GameEngine();
    Timer timer;
    Long lastTick = 0L;
    private StatePublisher statePublisher;


    private static final Logger LOG = LoggerFactory.getLogger(EngineController.class);

    public EngineController(){
        this.timer = new Timer();
        lastTick = System.currentTimeMillis();
        timer.scheduleAtFixedRate(
            new TimerTask() {
                @Override
                public void run() {
                    var newTick = System.currentTimeMillis();
                    engine.tick((double) (newTick - lastTick) / 1000);
                    var newState = engine.poll();
                    statePublisher.updateState(newState);
                    lastTick = newTick;
                }
            }
            ,TICK_RATE
            ,TICK_RATE
        );
    }

    public void setStatePublisher(StatePublisher publisher){
        this.statePublisher = publisher;
    }

    public synchronized void updateState(KeyDTO keysPresses){
        engine.processEvents(keysPresses);
    }

    public String addPlayer(){
        return this.engine.addPlayer();
    }

    private static final Integer TICK_RATE = 30;
}
