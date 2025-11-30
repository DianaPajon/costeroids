package com.itrsa.costeroids.controller;

import com.itrsa.costeroids.logic.dto.input.EventDTO;
import jakarta.inject.Singleton;

import java.util.LinkedList;
import java.util.concurrent.LinkedTransferQueue;

@Singleton
public class EventQueue {
    private final LinkedTransferQueue<EventDTO> arrivedEvents = new LinkedTransferQueue<>();

    public void offer(EventDTO event){
        this.arrivedEvents.offer(event);
    }

    public void drainTo(LinkedList<EventDTO> collection){
        arrivedEvents.drainTo(collection);
    }
}
