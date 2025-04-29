package com.rpsgame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PredictResponse {
    private String playerMove;
    private String opponentMove;
    private String result;
    private ScoreResponse score;
    private int roundNumber;

}
