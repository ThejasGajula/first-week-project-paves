package com.rpsgame.Model;


import java.util.*;

public class GameRoom {
    private final String roomId;
    private final Map<String, String> players = new HashMap<>(); // sessionId -> playerName
    private final Map<String, String> playersMoves = new HashMap<>();

    public static final int MAX_PLAYERS = 2;

    public GameRoom(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public void addPlayer(String sessionId, String playerName) {
        if (!isFull()) {
            players.put(sessionId, playerName);
        }
    }

    public List<String> getPlayerNames() {
        return new ArrayList<>(players.values());
    }

    public void addMove(String sessionId, String move) {
        playersMoves.put(sessionId, move);
    }

    public boolean isComplete() {
        return playersMoves.size() == 2;
    }

    public void reset() {
        playersMoves.clear();
    }

    public void submitMove(String sessionId, String move) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'submitMove'");
    }

    public boolean allMovesSubmitted() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'allMovesSubmitted'");
    }
}


