package com.rpsgame.controller;

public class ScoreResponse {
    private int wins;
    private int losses;
    private int draws;

    public ScoreResponse(int wins, int losses, int draws) {
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    // Getters
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getDraws() { return draws; }
}
