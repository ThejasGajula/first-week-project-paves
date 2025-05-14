package com.rpsgame.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameControllerHandles {

    private final SimpMessagingTemplate messagingTemplate;

    public GameControllerHandles(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    

    @MessageMapping("/play") // Maps to /app/play
    
    public void handleMove(@Payload String move, 
                           @Header("simpSessionId") String sessionId) {
        // Here you would implement logic for determining the result
        String result = "Received move: " + move;

        // Send the result back to all subscribers of /topic/result
        messagingTemplate.convertAndSend("/topic/result", result);
    }
}
