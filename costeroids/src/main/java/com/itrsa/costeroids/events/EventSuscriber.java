package com.itrsa.costeroids.events;

import com.itrsa.costeroids.controller.EventQueue;
import com.itrsa.costeroids.logic.dto.input.EventDTO;
import jakarta.inject.Singleton;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EventSuscriber implements Subscriber<EventDTO> {

    private final EventQueue eventQueue;
    private static final Logger log = LoggerFactory.getLogger(EventSuscriber.class);

    public EventSuscriber(EventQueue queue){
        eventQueue = queue;
    }


    @Override
    public void onSubscribe(Subscription s) {
        log.debug("onsuscribe");
    }

    @Override
    public void onNext(EventDTO EventDTO) {
        eventQueue.offer(EventDTO);
    }

    @Override
    public void onError(Throwable throwable) {
        log.debug("onerror");
    }

    @Override
    public void onComplete() {
        log.debug("oncomplete");
    }

}
