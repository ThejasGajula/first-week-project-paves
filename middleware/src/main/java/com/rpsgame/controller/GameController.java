package com.rpsgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/setup")
    public String setupPlayer(@RequestBody  PlayerSetupRequest request) {
        gameService.setupPlayer(request);
        return "Player setup successful!";
    }

    @GetMapping("/score")
    public ScoreResponse getScore() {
        return gameService.getScore();
    }

    @PostMapping("/submit")
    public PredictResult predict(@RequestBody ImageRequest request)
 {
        return gameService.predict(request.getImage(), request.getOpponentMove());
    }



    
        
}

