package com.itrsa.costeroids.state;

import com.itrsa.costeroids.logic.dto.output.StateDTO;
import io.micronaut.websocket.WebSocketSession;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateSuscriber implements Subscriber<StateDTO> {

    WebSocketSession session;
    Subscription subscription;
    boolean completed = false;
    private static final Logger LOG = LoggerFactory.getLogger(StateSuscriber.class);

    public StateSuscriber(WebSocketSession session){
        this.session = session;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
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
                        return t;
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
