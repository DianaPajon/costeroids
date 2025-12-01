package com.itrsa.costeroids.events;

import com.itrsa.costeroids.controller.EventQueue;
import com.itrsa.costeroids.logic.dto.input.EventDTO;
import com.itrsa.costeroids.logic.dto.input.EventType;
import io.micronaut.websocket.WebSocketSession;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class EventPublisher implements Publisher<EventDTO> {

    private final EventQueue eventQueue;
    private final Subscriber<? super EventDTO> subscriber;


    public EventPublisher(EventQueue eventQueue, EventSuscriber suscriber){
        this.eventQueue = eventQueue;
        this.subscriber = suscriber;
    }

    @Override
    public void subscribe(Subscriber<? super EventDTO> subscriber) {
        //
    }

    public String newPlayerEvent(WebSocketSession session){
        var id = this.eventQueue.addPlayer(session);
        var event = new EventDTO(EventType.NEW_PLAYER_EVENT, id);
        subscriber.onNext(event);
        return id;
    }

    public void keyPressEvent(EventDTO event){
        subscriber.onNext(event);
    }
}
