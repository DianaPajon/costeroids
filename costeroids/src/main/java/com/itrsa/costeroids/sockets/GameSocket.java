package com.itrsa.costeroids.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrsa.costeroids.controller.GameController;
import com.itrsa.costeroids.controller.KeySuscriber;
import com.itrsa.costeroids.logic.dto.input.KeyDTO;
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

    private final GameController controller;
    private final KeySuscriber suscriber;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(GameSocket.class);

    public GameSocket(GameController controller, KeySuscriber suscriber) {
        this.controller = controller;
        this.suscriber = suscriber;
        LOG.debug("socket creado!");
    }

    @OnOpen
    public Publisher<String> onOpen(String username, WebSocketSession session) {
        LOG.debug("socket abierto!");
        return session.send(controller.addPlayer(username, session));
    }

    @OnClose
    public void onClose(String username, WebSocketSession session) {
        LOG.debug("socket cerrado!");
        controller.closeSession(username);
    }

    @OnMessage
    public Publisher<String> onMessage(String message,String username, WebSocketSession session) throws JsonMappingException, JsonProcessingException {
        suscriber.onNext(mapper.readValue(message, KeyDTO.class));
        return null;
    }


}