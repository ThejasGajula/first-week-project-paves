package com.rpsgame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/predict")
    public PredictResult predict(@RequestParam("image") MultipartFile image,
                                  @RequestParam("opponentMove") String opponentMove) {
        return gameService.predict(image, opponentMove);
    }
        
}

