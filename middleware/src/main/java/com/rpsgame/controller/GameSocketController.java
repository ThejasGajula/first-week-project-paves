package com.rpsgame.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.rpsgame.Model.GameSession;
import com.rpsgame.Model.Move;
import com.rpsgame.Model.RoundResult;
import com.rpsgame.Service.GameService;
import com.rpsgame.controller.GameSocketController.GameReadyMessage;

import java.util.Map;
import java.util.Optional;
@Controller
public class GameSocketController {

    @Autowired
    private GameService gameService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    // Create a new multiplayer game
    @MessageMapping("/create-game")
    public void createGame(CreateGameRequest request) {
        GameSession session = gameService.createMultiplayerGame(request.getPlayerId(), request.getMaxRounds());
        
        // Send response back to the creator
        messagingTemplate.convertAndSendToUser(
            request.getPlayerId(),
            "/topic/game-created", 
            Map.of("sessionId", session.getSessionId())
        );
        
        // Broadcast to waiting room
        messagingTemplate.convertAndSend(
            "/topic/available-games",
            gameService.getAvailableGames()
        );
    }
    
    // Join an available game
    @MessageMapping("/join-game")
    public void joinGame(JoinGameRequest request) {
        Optional<GameSession> joinedGame;
        
        if (request.getSessionId() != null && !request.getSessionId().isEmpty()) {
            // Join specific game
            joinedGame = gameService.joinSpecificGame(request.getSessionId(), request.getPlayerId());
        } else {
            // Join any available game
            joinedGame = gameService.joinGame(request.getPlayerId());
        }
        
        joinedGame.ifPresent(session -> {
            // Notify both players the game is ready
            GameReadyMessage readyMessage = new GameReadyMessage(session);
            
            messagingTemplate.convertAndSendToUser(
                session.getPlayer1Id(),
                "/topic/game-ready",
                readyMessage
            );
            
            messagingTemplate.convertAndSendToUser(
                session.getPlayer2Id(),
                "/topic/game-ready", 
                readyMessage
            );
            
            // Update available games list for everyone
            messagingTemplate.convertAndSend(
                "/topic/available-games",
                gameService.getAvailableGames()
            );
        });
        
        if (joinedGame.isEmpty()) {
            // No game available
            messagingTemplate.convertAndSendToUser(
                request.getPlayerId(),
                "/topic/join-failed",
                Map.of("message", "No available games to join")
            );
        }
    }
    
    // Submit a move in multiplayer game
    @MessageMapping("/games/{sessionId}/submit-move")
    public void submitMove(@DestinationVariable String sessionId, MoveRequest request) {
        try {
            gameService.submitMultiplayerMove(sessionId, request.getPlayerId(), request.getMove());
            
            // Check if both players have submitted their moves
            Optional<RoundResult> result = gameService.checkAndProcessRound(sessionId);
            
            result.ifPresent(roundResult -> {
                // Send result to both players
                messagingTemplate.convertAndSend(
                    "/topic/games/" + sessionId + "/result",
                    roundResult
                );
            });
        } catch (IllegalArgumentException e) {
            // Handle error - send error message to client
            messagingTemplate.convertAndSendToUser(
                request.getPlayerId(),
                "/topic/errors",
                Map.of("error", e.getMessage())
            );
        }
    }
    
    // Get available games for joining
    @MessageMapping("/get-available-games")
    @SendTo("/topic/available-games")
    public Map<String, GameSession> getAvailableGames() {
        return gameService.getAvailableGames();
    }
    
    // Helper classes for requests and responses
    
    public static class CreateGameRequest {
        private String playerId;
        private int maxRounds;
        
        public String getPlayerId() { return playerId; }
        public void setPlayerId(String playerId) { this.playerId = playerId; }
        
        public int getMaxRounds() { return maxRounds; }
        public void setMaxRounds(int maxRounds) { this.maxRounds = maxRounds; }
    }
    
    public static class JoinGameRequest {
        private String playerId;
        private String sessionId; // Optional, for joining specific game
        
        public String getPlayerId() { return playerId; }
        public void setPlayerId(String playerId) { this.playerId = playerId; }
        
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    }
    
    public static class MoveRequest {
        private String playerId;
        private Move move;
        
        public String getPlayerId() { return playerId; }
        public void setPlayerId(String playerId) { this.playerId = playerId; }
        
        public Move getMove() { return move; }
        public void setMove(Move move) { this.move = move; }
    }
    
    public static class GameReadyMessage {
        private String sessionId;
        private String player1Id;
        private String player2Id;
        private int maxRounds;
        
        public GameReadyMessage(GameSession session) {
            this.sessionId = session.getSessionId();
            this.player1Id = session.getPlayer1Id();
            this.player2Id = session.getPlayer2Id();
            this.maxRounds = session.getMaxRounds();
        }
        
        public String getSessionId() { return sessionId; }
        public String getPlayer1Id() { return player1Id; }
        public String getPlayer2Id() { return player2Id; }
        public int getMaxRounds() { return maxRounds; }
    }
}