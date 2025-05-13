package com.rpsgame.Service;



import com.rpsgame.Model.GameRoom;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MatchmakingService {

    private final Queue<String> waitingQueue = new LinkedList<>();
    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private final Map<String, String> sessionToRoom = new ConcurrentHashMap<>();
    private final AtomicInteger roomCounter = new AtomicInteger(1);

    public synchronized String assignRoom(String sessionId) {
        // If the player is already in a room, return it
        if (sessionToRoom.containsKey(sessionId)) {
            return sessionToRoom.get(sessionId);
        }

        // Try to find a room with space
        for (GameRoom room : rooms.values()) {
            if (!room.isFull()) {
                sessionToRoom.put(sessionId, room.getRoomId());
                return room.getRoomId();
            }
        }

        // No available room → create a new one
        String roomId = "room-" + roomCounter.getAndIncrement();
        GameRoom newRoom = new GameRoom(roomId);
        rooms.put(roomId, newRoom);
        sessionToRoom.put(sessionId, roomId);
        return roomId;
    }

    public boolean addPlayerToRoom(String sessionId, String playerName) {
        String roomId = sessionToRoom.get(sessionId);
        GameRoom room = rooms.get(roomId);

        if (room == null) return false;
        if (room.isFull()) return false;

        room.addPlayer(sessionId, playerName);
        return true;
    }

    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public String getRoomId(String sessionId) {
        return sessionToRoom.get(sessionId);
    }

    public void removeSession(String sessionId) {
        String roomId = sessionToRoom.remove(sessionId);
        if (roomId != null) {
            GameRoom room = rooms.get(roomId);
            if (room != null) {
                room.getPlayers().remove(sessionId);
            }
        }
        waitingQueue.remove(sessionId);
    }
}
