package com.rpsgame.Model;



public enum Move {
    ROCK,
    PAPER,
    SCISSORS,
    NONE; // Used when no move has been made yet
    
    public static Move getRandomMove() {
        int pick = (int) (Math.random() * 3);
        return values()[pick];
    }
    
    public static Move getWinner(Move move1, Move move2) {
        if (move1 == move2) {
            return null; // Draw
        }
        
        if ((move1 == ROCK && move2 == SCISSORS) ||
            (move1 == PAPER && move2 == ROCK) ||
            (move1 == SCISSORS && move2 == PAPER)) {
            return move1;
        } else {
            return move2;
        }
    }
}