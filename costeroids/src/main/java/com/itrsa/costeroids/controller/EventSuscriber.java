package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import jakarta.inject.Singleton;

import java.util.concurrent.Flow;

@Singleton
public class EventSuscriber implements Flow.Subscriber<EventDTO> {

    private final EngineController engineController;

    public EventSuscriber(EngineController controller){
        engineController = controller;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

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
