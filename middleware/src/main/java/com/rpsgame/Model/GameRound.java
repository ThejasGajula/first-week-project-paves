package com.rpsgame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GameRound {
    private int roundNumber;
    private String roomId;
    private String winnerId;


    public GameRound(int roundNumber, String roomId) {
        this.roundNumber = roundNumber;
        this.roomId = roomId;
    }
}
