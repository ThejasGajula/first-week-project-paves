package com.rpsgame.controller;

import org.springframework.stereotype.Service;

import lombok.Data;

import java.util.Random;

@Service
@Data
public class GameService {

    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    

    private final String[] moves = {"rock", "paper", "scissors"};
    private PlayerSetupRequest playerProfile;

    public void setupPlayer(PlayerSetupRequest setupRequest) {
        this.playerProfile = setupRequest;
    }

    public ScoreResponse getScore() {
        return new ScoreResponse(wins, losses, draws);
    }

    public PredictResult predict(String image, String opponentMove) {
        // Dummy logic: randomly pick a move (real: should analyze image)
        String playerMove = moves[new Random().nextInt(moves.length)];

        String result = determineResult(playerMove, opponentMove.toLowerCase());

        // Update score
        switch (result) {
            case "Win":
                wins++;
                break;
            case "Loss":
                losses++;
                break;
            case "Draw":
                draws++;
                break;
        }

        return new PredictResult(playerMove, opponentMove, result);
    }

    private String determineResult(String playerMove, String opponentMove) {
        if (playerMove.equals(opponentMove)) return "Draw";
        switch (playerMove) {
            case "rock":
                return opponentMove.equals("scissors") ? "Win" : "Loss";
            case "paper":
                return opponentMove.equals("rock") ? "Win" : "Loss";
            case "scissors":
                return opponentMove.equals("paper") ? "Win" : "Loss";
            default:
                return "Invalid";
        }
    }
}
