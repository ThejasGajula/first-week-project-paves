package com.rpsgame.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.rpsgame.Model.GameRoom;
import com.rpsgame.Service.MatchmakingService;

import java.util.List;

@Controller
public class GameControllerRoom {

    private final SimpMessagingTemplate messagingTemplate;
    private final MatchmakingService matchmakingService;

    public GameControllerRoom(SimpMessagingTemplate messagingTemplate, MatchmakingService matchmakingService) {
        this.messagingTemplate = messagingTemplate;
        this.matchmakingService = matchmakingService;
    }

    @MessageMapping("/join")
    public void handlePlayerJoin(@Payload PlayerJoinRequest joinRequest,
                                 @Header("simpSessionId") String sessionId) {

        String roomId = matchmakingService.assignRoom(sessionId);
        GameRoom room = matchmakingService.getRoom(roomId);

        if (room == null || room.isFull()) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/updates", "Room is full. Try again later.");
            return;
        }

        boolean added = matchmakingService.addPlayerToRoom(sessionId, joinRequest.getPlayerName());

        if (!added) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/updates", "Failed to join. Room may be full.");
            return;
        }

        // Broadcast updated player list
        List<String> playerNames = room.getPlayerNames();
        messagingTemplate.convertAndSend("/topic/room/" + roomId, playerNames);

        // Notify this user
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/updates", "Joined room: " + roomId);
    }
}
