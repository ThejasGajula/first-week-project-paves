package com.rpsgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rpsgame.Model.GameSession;
import com.rpsgame.Model.Move;
import com.rpsgame.Model.RoundResult;
import com.rpsgame.Service.GameService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameRestController {

    @Autowired
    private GameService gameService;
    
    // Create single player game
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createGame(@RequestBody Map<String, Object> request) {
        String playerId = (String) request.get("playerId");
        int maxRounds = Integer.parseInt(request.get("maxRounds").toString());
        
        GameSession session = gameService.createSinglePlayerGame(playerId, maxRounds);
        
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getSessionId());
        response.put("message", "Game created successfully");
        return ResponseEntity.ok(response);
    }
    
    // Ready endpoint for single player - signals player is ready to play
    @GetMapping("/{sessionId}/ready")
    public ResponseEntity<Map<String, Object>> ready(@PathVariable String sessionId) {
        return gameService.getSession(sessionId)
            .map(session -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("status", "ready");
                responseBody.put("message", "Ready to play");
                responseBody.put("maxRounds", session.getMaxRounds());
                responseBody.put("currentRound", session.getCurrentRound() + 1);
                return ResponseEntity.ok(responseBody);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Game session not found")));
    }
    
    // Submit move for single player against AI
    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<RoundResult> submitMove(
            @PathVariable String sessionId,
            @RequestBody Map<String, Object> request) {
        
        String playerId = (String) request.get("playerId");
        Move playerMove = Move.valueOf(((String) request.get("playerMove")).toUpperCase());
        
        try {
            RoundResult result = gameService.submitSinglePlayerMove(sessionId, playerId, playerMove);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get game status and scores
    @GetMapping("/{sessionId}/status")
    public ResponseEntity<Map<String, Object>> getGameStatus(@PathVariable String sessionId) {
        return gameService.getSession(sessionId)
            .map(session -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("sessionId", session.getSessionId());
                responseBody.put("player1Score", session.getPlayer1Score());
                responseBody.put("player2Score", session.getPlayer2Score());
                responseBody.put("draws", session.getDraws());
                responseBody.put("currentRound", session.getCurrentRound());
                responseBody.put("maxRounds", session.getMaxRounds());
                responseBody.put("isGameOver", session.isGameOver());
                responseBody.put("isSinglePlayer", session.isSinglePlayer());
                return ResponseEntity.ok(responseBody);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Game session not found")));
    }
}