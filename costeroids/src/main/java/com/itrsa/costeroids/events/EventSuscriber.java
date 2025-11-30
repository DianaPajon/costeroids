package com.itrsa.costeroids.events;

import com.itrsa.costeroids.controller.EngineController;
import com.itrsa.costeroids.logic.dto.input.EventDTO;
import jakarta.inject.Singleton;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Singleton
public class EventSuscriber implements Subscriber<EventDTO> {

    private final EngineController engineController;

    public EventSuscriber(EngineController controller){
        engineController = controller;
    }


    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(EventDTO EventDTOS) {
        engineController.updateState(EventDTOS);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

}
