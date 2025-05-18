package com.rpsgame.Model;

import java.util.UUID;

public class GameSession {
    private String sessionId;
    private String player1Id;
    private String player2Id; // null for single player mode
    private boolean isSinglePlayer;
    private int maxRounds;
    private int currentRound;
    private int player1Score;
    private int player2Score; // AI or another player
    private int draws;
    private GameStatus status;
    
    // For multiplayer waiting room
    private long createdAt;
    
    public GameSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.currentRound = 0;
        this.player1Score = 0;
        this.player2Score = 0;
        this.draws = 0;
        this.status = GameStatus.WAITING;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Constructor for single player mode
    public GameSession(String playerId, int maxRounds) {
        this();
        this.player1Id = playerId;
        this.isSinglePlayer = true;
        this.maxRounds = maxRounds;
        this.status = GameStatus.READY;
    }
    
    // Constructor for creating a multiplayer session
    public GameSession(String player1Id, int maxRounds, boolean isSinglePlayer) {
        this();
        this.player1Id = player1Id;
        this.isSinglePlayer = isSinglePlayer;
        this.maxRounds = maxRounds;
    }
    
    public boolean isGameOver() {
        return currentRound >= maxRounds || status == GameStatus.FINISHED;
    }
    
    public boolean canJoin() {
        return !isSinglePlayer && player2Id == null && status == GameStatus.WAITING;
    }
    
    public void addPlayer2(String player2Id) {
        if (canJoin()) {
            this.player2Id = player2Id;
            this.status = GameStatus.READY;
        } else {
            throw new IllegalStateException("Cannot join this game session");
        }
    }
    
    // Getters and setters
    public String getSessionId() {
        return sessionId;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }
    
    public void incrementRound() {
        this.currentRound++;
        if (this.currentRound >= this.maxRounds) {
            this.status = GameStatus.FINISHED;
        }
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void incrementPlayer1Score() {
        this.player1Score++;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void incrementPlayer2Score() {
        this.player2Score++;
    }

    public int getDraws() {
        return draws;
    }

    public void incrementDraws() {
        this.draws++;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
}