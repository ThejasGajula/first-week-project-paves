package com.rpsgame.Model;

public class RoundResult {
    private String sessionId;
    private int roundNumber;
    private Move player1Move;
    private Move player2Move;
    private String winner; // "player1", "player2", or "draw"
    private int player1Score;
    private int player2Score;
    private int draws;
    private boolean isGameOver;
    
    // Default constructor for Jackson
    public RoundResult() {}
    
    public RoundResult(GameSession session, Move player1Move, Move player2Move) {
        this.sessionId = session.getSessionId();
        this.roundNumber = session.getCurrentRound();
        this.player1Move = player1Move;
        this.player2Move = player2Move;
        
        Move winningMove = Move.getWinner(player1Move, player2Move);
        
        if (winningMove == null) {
            this.winner = "draw";
            session.incrementDraws();
        } else if (winningMove == player1Move) {
            this.winner = "player1";
            session.incrementPlayer1Score();
        } else {
            this.winner = "player2";
            session.incrementPlayer2Score();
        }
        
        session.incrementRound();
        
        this.player1Score = session.getPlayer1Score();
        this.player2Score = session.getPlayer2Score();
        this.draws = session.getDraws();
        this.isGameOver = session.isGameOver();
    }
    
    // Getters
    public String getSessionId() {
        return sessionId;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
    
    public Move getPlayer1Move() {
        return player1Move;
    }
    
    public Move getPlayer2Move() {
        return player2Move;
    }
    
    public String getWinner() {
        return winner;
    }
    
    public int getPlayer1Score() {
        return player1Score;
    }
    
    public int getPlayer2Score() {
        return player2Score;
    }
    
    public int getDraws() {
        return draws;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
}
