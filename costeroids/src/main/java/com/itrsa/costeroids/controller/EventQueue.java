package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import com.itrsa.costeroids.logic.dto.input.EventType;
import com.itrsa.costeroids.state.StatePublisher;
import com.itrsa.costeroids.state.StateSuscriber;
import io.micronaut.websocket.WebSocketSession;
import jakarta.inject.Singleton;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;

@Singleton
public class EventQueue {
    private final LinkedTransferQueue<EventDTO> arrivedEvents = new LinkedTransferQueue<>();
    private final StatePublisher statePublisher;

    public EventQueue(StatePublisher statePublisher){
        this.statePublisher = statePublisher;
    }

    public void offer(EventDTO event){
        this.arrivedEvents.offer(event);
    }

    public void drainTo(LinkedList<EventDTO> collection){
        arrivedEvents.drainTo(collection);
    }


    public String addPlayer(String username, WebSocketSession session){
        var id = UUID.randomUUID().toString();
        var eventDTO = new EventDTO(EventType.NEW_PLAYER_EVENT, id);
        arrivedEvents.offer(eventDTO);
        var subscriber = new StateSuscriber(session);
        this.statePublisher.subscribe(subscriber);
        return id;
    }
}
