package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.KeyDTO;
import io.micronaut.websocket.WebSocketSession;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class KeyPublisher implements Flow.Publisher<List<KeyDTO>> {

    private final EngineController controller;
    private List<Flow.Subscriber<? super List<KeyDTO>>> subscribers;


    public KeyPublisher(EngineController controller){
        this.controller = controller;
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super List<KeyDTO>> subscriber) {
        this.subscribers.add(subscriber);
    }

    public void UpdateState(List<KeyDTO> newState){
        this.subscribers.forEach(
                subscriber -> {
                    subscriber.onNext(newState);
                }
        );
    }
}
