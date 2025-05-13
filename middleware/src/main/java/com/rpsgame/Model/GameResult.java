package com.rpsgame.Model;

import java.util.List;

public class GameResult {
    private String winner;
    private List<String> players;
    private String message;

    public GameResult(String winner, List<String> players, String message) {
        this.winner = winner;
        this.players = players;
        this.message = message;
    }

    public String getWinner() { return winner; }
    public List<String> getPlayers() { return players; }
    public String getMessage() { return message; }
}

