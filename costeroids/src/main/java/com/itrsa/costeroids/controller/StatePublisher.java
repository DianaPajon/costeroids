package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.output.StateDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class StatePublisher implements Flow.Publisher<StateDTO> {

    List<Flow.Subscriber<? super StateDTO>> subscribers = new ArrayList<>();
    List<Flow.Subscriber<? super StateDTO>> removed= new ArrayList<>();
    @Override
    public void subscribe(Flow.Subscriber<? super StateDTO> subscriber) {
        this.subscribers.add(subscriber);
        subscriber.onSubscribe(
                new Flow.Subscription() {
                    @Override
                    public void request(long l) {

                    }

                    @Override
                    public void cancel() {
                        removed.add(subscriber);
                    }
                }
        );
    }

    public void updateState(StateDTO state){
        subscribers.forEach(
                (subscriber -> {
                    if(!removed.contains(subscriber)){
                        subscriber.onNext(state);
                    }
                })
        );
        subscribers.removeAll(removed);
        removed.clear();
    }
}

