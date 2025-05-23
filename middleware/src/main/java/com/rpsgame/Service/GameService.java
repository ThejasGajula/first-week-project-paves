package com.rpsgame.Service;

import org.springframework.stereotype.Service;

import com.rpsgame.Model.PlayerSetupRequest;
import com.rpsgame.Model.PredictResponse;
import com.rpsgame.Model.ScoreResponse;

import lombok.Data;


@Service
@Data
public class GameService {

    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    

    private final String[] moves = {"rock", "paper", "scissors"};
    private PlayerSetupRequest playerProfile;
    private int currentRound=0;

    public void setupPlayer(PlayerSetupRequest setupRequest) {
        this.playerProfile = setupRequest;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.currentRound = 0;
    }

    public ScoreResponse getScore() {
        return new ScoreResponse(wins, losses, draws);
    }

    public PredictResponse predict(String playerMove, String opponentMove) {
        
        String result = determineResult(playerMove, opponentMove.toLowerCase());

        if (currentRound<=this.playerProfile.getRounds()) {
            currentRound++;
        }

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

        
        return new PredictResponse(playerMove, opponentMove, result ,getScore(),currentRound) {
        };
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

    public void restartGame() {
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.currentRound = 0;
    }
}
