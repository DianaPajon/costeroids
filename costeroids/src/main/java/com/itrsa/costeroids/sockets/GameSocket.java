package com.itrsa.costeroids.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrsa.costeroids.controller.EventQueue;
import com.itrsa.costeroids.events.EventPublisher;
import com.itrsa.costeroids.events.EventSuscriber;
import com.itrsa.costeroids.logic.dto.input.EventDTO;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerWebSocket("/ws/game/player")
@Prototype
public class GameSocket {

    private final EventQueue eventQueue;
    private final EventSuscriber suscriber;
    private final EventPublisher publisher;
    private String playerId;
    private static final Logger LOG = LoggerFactory.getLogger(GameSocket.class);

    public GameSocket(EventQueue eventQueue, EventSuscriber suscriber) {
        this.eventQueue = eventQueue;
        this.suscriber = suscriber;
        this.publisher = new EventPublisher(eventQueue, suscriber);
        LOG.debug("socket creado!");
    }

    @OnOpen
    public Publisher<String> onOpen(WebSocketSession session) {
        this.playerId = publisher.newPlayerEvent(session);
        return session.send(playerId);
    }

    @OnClose
    public void onClose(WebSocketSession session) {
        LOG.debug("socket cerrado!");
        suscriber.onComplete();
    }

    @OnMessage
    public Publisher<String> onMessage(EventDTO message,WebSocketSession session) throws JsonMappingException, JsonProcessingException {
       if(this.playerId.equalsIgnoreCase(message.getPlayerId())) publisher.keyPressEvent(message);
        return null;
    }


}