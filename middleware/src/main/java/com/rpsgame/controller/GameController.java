package com.rpsgame.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rpsgame.Model.Player;
import com.rpsgame.Model.PlayerSetupRequest;
import com.rpsgame.Model.PredictResponse;
import com.rpsgame.Model.ScoreResponse;
import com.rpsgame.Service.GameService;


@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/setup")
    public String setupPlayer(@RequestBody PlayerSetupRequest request) {
        gameService.setupPlayer(request);
        return "Player setup successful!";
    }

    @GetMapping("/score")
    public ScoreResponse getScore() {
        return gameService.getScore();
    }

    @PostMapping("/submit")
    public PredictResponse predict(@RequestBody Map<String, String> request) {
        return gameService.predict(request.get("playerMove"), request.get("opponentMove"));
    }

    @GetMapping("/restart")
    public ResponseEntity restart() {
        gameService.restartGame();
        return ResponseEntity.ok("Game restarted successfully!");
    }
    @PostMapping("/room/ready")
    public void toggleReadiness(@RequestBody Map<String, String> request) {
        String playerId = request.get("playerId");
        Player updatedPlayer = gameService.togglePlayerReady(playerId);

        // Broadcast updated readiness states
        messagingTemplate.convertAndSend("/topic/room/readiness", gameService.getAllPlayers());
    }
}
