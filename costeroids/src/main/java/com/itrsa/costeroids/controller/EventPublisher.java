package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import com.itrsa.costeroids.logic.dto.input.EventType;
import io.micronaut.websocket.WebSocketSession;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class EventPublisher implements Publisher<EventDTO> {

    private final EngineController controller;
    private List<Subscriber<? super EventDTO>> subscribers;


    public EventPublisher(EngineController controller){
        this.controller = controller;
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(Subscriber<? super EventDTO> subscriber) {
        this.subscribers.add(subscriber);
    }

    public String newPlayerEvent(WebSocketSession session, String username){
        var id = this.controller.addPlayer(username, session);
        var event = new EventDTO(EventType.NEW_PLAYER_EVENT, id);
        this.subscribers.forEach(
                (subscriber -> {
                    subscriber.onNext(event);
                })
        );
        return id;
    }

    public void keyPressEvent(EventDTO event){
        this.subscribers.forEach(
                (subscriber -> {
                    subscriber.onNext(event);
                })
        );
    }
}
