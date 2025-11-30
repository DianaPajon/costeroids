package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.output.StateDTO;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Flow;

public class StatePublisher implements Publisher<StateDTO> {

    ConcurrentLinkedDeque<Subscriber<? super StateDTO>> subscribers = new ConcurrentLinkedDeque<>();

    @Override
    public void subscribe(Subscriber<? super StateDTO> subscriber) {
        this.subscribers.add(subscriber);
        subscriber.onSubscribe(
                new Subscription() {
                    @Override
                    public void request(long l) {

                    }

                    @Override
                    public void cancel() {
                        subscribers.remove(subscriber);
                    }
                }
        );
    }

    public void updateState(StateDTO state) {
        subscribers.forEach(
                (subscriber -> {
                    subscriber.onNext(state);
                })
        );
    }
}

