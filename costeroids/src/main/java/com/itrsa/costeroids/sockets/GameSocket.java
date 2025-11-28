package com.itrsa.costeroids.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrsa.costeroids.controller.EngineController;
import com.itrsa.costeroids.controller.EventPublisher;
import com.itrsa.costeroids.controller.EventSuscriber;
import com.itrsa.costeroids.logic.dto.input.EventDTO;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerWebSocket("/ws/game/{username}")
public class GameSocket {

    private final EngineController engineController;
    private final EventSuscriber suscriber;
    private final EventPublisher publisher;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(GameSocket.class);

    public GameSocket(EngineController engineController,  EventSuscriber suscriber) {
        this.engineController = engineController;
        this.suscriber = suscriber;
        this.publisher = new EventPublisher(engineController);
        publisher.subscribe(suscriber);
        LOG.debug("socket creado!");
    }

    @OnOpen
    public Publisher<String> onOpen(String username, WebSocketSession session) {
        return session.send(publisher.newPlayerEvent(session, username));
    }

    @OnClose
    public void onClose(String username, WebSocketSession session) {
        LOG.debug("socket cerrado!");
        engineController.closeSession(username);
    }

    @OnMessage
    public Publisher<String> onMessage(String message,String username, WebSocketSession session) throws JsonMappingException, JsonProcessingException {
        publisher.keyPressEvent(mapper.readValue(message, EventDTO.class));
        return null;
    }


}