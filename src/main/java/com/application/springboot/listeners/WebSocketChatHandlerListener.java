package com.application.springboot.listeners;

import com.application.springboot.model.websocket.WebSocketChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketChatHandlerListener {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;



    @EventListener
    public void handleWebSocketConnectionLister(SessionConnectedEvent sessionConnectedEvent){
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionConnectedEvent sessionConnectedEvent){
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
        if(username !=null){
            WebSocketChatMessage webSocketChatMessage = new WebSocketChatMessage();
            webSocketChatMessage.setType("Leave");
            webSocketChatMessage.setSender("username");
            messageSendingOperations.convertAndSend("/message/public",webSocketChatMessage);
        }
    }



}
