package com.rpsgame.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
