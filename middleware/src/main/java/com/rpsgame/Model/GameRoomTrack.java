package com.rpsgame.Model;


import java.util.*;

public class GameRoomTrack {
    public static final int MAX_PLAYERS = 2;

    private final String roomId;
    private final Map<String, String> players = new HashMap<>();         // sessionId -> name
    private final Set<String> readyPlayers = new HashSet<>();            // sessionId
    private final Map<String, String> moves = new HashMap<>();           // sessionId -> move
    private String winnerName;

    public GameRoomTrack(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() { return roomId; }
    public Map<String, String> getPlayers() { return players; }
    public Set<String> getReadyPlayers() { return readyPlayers; }
    public Map<String, String> getMoves() { return moves; }
    public String getWinnerName() { return winnerName; }

    public boolean isFull() { return players.size() >= MAX_PLAYERS; }

    public void addPlayer(String sessionId, String name) {
        if (!isFull()) players.put(sessionId, name);
    }

    public void markReady(String sessionId) {
        if (players.containsKey(sessionId)) {
            readyPlayers.add(sessionId);
        }
    }

    public boolean allReady() {
        return readyPlayers.size() == MAX_PLAYERS;
    }

    public void submitMove(String sessionId, String move) {
        moves.put(sessionId, move);
    }

    public boolean allMovesSubmitted() {
        return moves.size() == MAX_PLAYERS;
    }

    public void computeResult() {
        if (moves.size() < 2) return;

        List<String> sessionIds = new ArrayList<>(moves.keySet());
        String p1 = sessionIds.get(0);
        String p2 = sessionIds.get(1);
        String move1 = moves.get(p1);
        String move2 = moves.get(p2);

        int result = compareMoves(move1, move2);
        if (result == 0) {
            winnerName = "Draw";
        } else {
            String winnerId = (result == 1) ? p1 : p2;
            winnerName = players.get(winnerId);
        }
    }

    public void resetGame() {
        moves.clear();
        readyPlayers.clear();
        winnerName = null;
    }

    private int compareMoves(String m1, String m2) {
        if (m1.equals(m2)) return 0;
        if ((m1.equals("rock") && m2.equals("scissors")) ||
            (m1.equals("paper") && m2.equals("rock")) ||
            (m1.equals("scissors") && m2.equals("paper"))) {
            return 1;
        }
        return -1;
    }
}

