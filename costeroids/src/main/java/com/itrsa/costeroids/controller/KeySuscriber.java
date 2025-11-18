package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.KeyDTO;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.concurrent.Flow;

@Singleton
public class KeySuscriber implements Flow.Subscriber<KeyDTO> {

    private final EngineController engineController;

    public KeySuscriber(EngineController controller){
        engineController = controller;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(KeyDTO keyDTOS) {
        engineController.updateState(keyDTOS);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

}
