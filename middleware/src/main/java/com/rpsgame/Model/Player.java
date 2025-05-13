package com.rpsgame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Player {
    private String playerId;
    private String playerName;
    private int score;
    private boolean isReady;

    
    public Player(String playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = 0; // Default score
        this.isReady = false;
    }
    public void toggleReady() {
        this.isReady = !this.isReady;
    }

    
}
