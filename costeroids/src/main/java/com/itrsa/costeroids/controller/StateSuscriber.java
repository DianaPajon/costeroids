package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.KeyDTO;
import com.itrsa.costeroids.logic.dto.output.StateDTO;
import com.itrsa.costeroids.logic.engine.GameEngine;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.exceptions.WebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
public class StateSuscriber implements Flow.Subscriber<StateDTO> {

    WebSocketSession session;
    Flow.Subscription subscription;
    boolean completed = false;
    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    public StateSuscriber(WebSocketSession session){
        this.session = session;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public synchronized void onNext(StateDTO newState) {
        if(completed) return;
        try{
            session.sendAsync(newState);
        } catch (WebSocketException e) {
            //desuscribir ante sesión cerrada
            if(!session.isOpen()){
                session.close();
                subscription.cancel();;
            } else{
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {
        subscription.cancel();;
        completed = true;
    }
}
