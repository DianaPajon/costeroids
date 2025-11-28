package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.output.StateDTO;
import io.micronaut.websocket.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;
public class StateSuscriber implements Flow.Subscriber<StateDTO> {

    WebSocketSession session;
    Flow.Subscription subscription;
    boolean completed = false;
    private static final Logger LOG = LoggerFactory.getLogger(StateSuscriber.class);

    public StateSuscriber(WebSocketSession session){
        this.session = session;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onNext(StateDTO newState) {
        if(completed) return;
        try{
            session.sendAsync(newState).handle(
                    (t, state) -> {
                        if(!session.isOpen()){
                            session.close();
                            subscription.cancel();;
                        }
                        return state;
                    }
            );
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
