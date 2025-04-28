package com.rpsgame.controller;

public class PredictResult {
    private String playerMove;
    private String opponentMove;
    private String result;

    public PredictResult(String playerMove, String opponentMove, String result) {
        this.playerMove = playerMove;
        this.opponentMove = opponentMove;
        this.result = result;
    }

    // Getters
    public String getPlayerMove() { return playerMove; }
    public String getOpponentMove() { return opponentMove; }
    public String getResult() { return result; }
}
