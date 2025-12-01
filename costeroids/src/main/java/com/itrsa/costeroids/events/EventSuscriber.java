package com.itrsa.costeroids.events;

import com.itrsa.costeroids.controller.EventQueue;
import com.itrsa.costeroids.logic.dto.input.EventDTO;
import jakarta.inject.Singleton;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Singleton
public class EventSuscriber implements Subscriber<EventDTO> {

    private final EventQueue eventQueue;

    public EventSuscriber(EventQueue queue){
        eventQueue = queue;
    }


    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(EventDTO EventDTO) {
        eventQueue.offer(EventDTO);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

}
