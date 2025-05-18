package com.rpsgame.Service;

import org.springframework.stereotype.Service;

import com.rpsgame.Model.GameSession;
import com.rpsgame.Model.GameStatus;
import com.rpsgame.Model.Move;
import com.rpsgame.Model.RoundResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameService {
    
    // Store all active game sessions
    private final ConcurrentHashMap<String, GameSession> gameSessions = new ConcurrentHashMap<>();
    
    // Store current moves for each player in a session
    private final ConcurrentHashMap<String, Map<String, Move>> sessionMoves = new ConcurrentHashMap<>();
    
    // Create a single player game
    public GameSession createSinglePlayerGame(String playerId, int maxRounds) {
        GameSession session = new GameSession(playerId, maxRounds);
        gameSessions.put(session.getSessionId(), session);
        sessionMoves.put(session.getSessionId(), new HashMap<>());
        return session;
    }
    
    // Create a multiplayer game waiting for another player
    public GameSession createMultiplayerGame(String playerId, int maxRounds) {
        GameSession session = new GameSession(playerId, maxRounds, false);
        gameSessions.put(session.getSessionId(), session);
        sessionMoves.put(session.getSessionId(), new HashMap<>());
        return session;
    }
    
    // Join an existing multiplayer game
    public Optional<GameSession> joinGame(String playerId) {
        // Find an available session
        Optional<GameSession> availableSession = gameSessions.values().stream()
                .filter(GameSession::canJoin)
                .findFirst();
                
        if (availableSession.isPresent()) {
            GameSession session = availableSession.get();
            session.addPlayer2(playerId);
            return Optional.of(session);
        }
        
        return Optional.empty();
    }
    
    // Join a specific game by ID
    public Optional<GameSession> joinSpecificGame(String sessionId, String playerId) {
        GameSession session = gameSessions.get(sessionId);
        if (session != null && session.canJoin()) {
            session.addPlayer2(playerId);
            return Optional.of(session);
        }
        return Optional.empty();
    }
    
    // Submit a move for single player
    public RoundResult submitSinglePlayerMove(String sessionId, String playerId, Move playerMove) {
        GameSession session = gameSessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found with ID: " + sessionId);
        }
        
        if (!session.isSinglePlayer() || !playerId.equals(session.getPlayer1Id())) {
            throw new IllegalArgumentException("Invalid session or player");
        }
        
        // Generate AI move
        Move aiMove = Move.getRandomMove();
        
        // Calculate and update results
        RoundResult result = new RoundResult(session, playerMove, aiMove);
        
        // Clean up if game is over
        if (result.isGameOver()) {
            session.setStatus(GameStatus.FINISHED);
        }
        
        return result;
    }
    
    // Submit a move for multiplayer
    public void submitMultiplayerMove(String sessionId, String playerId, Move playerMove) {
        GameSession session = gameSessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found: " + sessionId);
        }
        
        if (session.isSinglePlayer()) {
            throw new IllegalArgumentException("Cannot submit multiplayer move to single player session");
        }
        
        // Validate that the player belongs to this session
        if (!playerId.equals(session.getPlayer1Id()) && !playerId.equals(session.getPlayer2Id())) {
            throw new IllegalArgumentException("Player not part of this game session");
        }
        
        // Store the move
        Map<String, Move> moves = sessionMoves.computeIfAbsent(sessionId, k -> new HashMap<>());
        moves.put(playerId, playerMove);
    }
    
    // Check if both players have submitted their moves and calculate the result
    public Optional<RoundResult> checkAndProcessRound(String sessionId) {
        GameSession session = gameSessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found: " + sessionId);
        }
        
        Map<String, Move> moves = sessionMoves.get(sessionId);
        if (moves == null) {
            sessionMoves.put(sessionId, new HashMap<>());
            return Optional.empty();
        }
        
        String player1Id = session.getPlayer1Id();
        String player2Id = session.getPlayer2Id();
        
        // Check if both players have submitted moves
        if (moves.containsKey(player1Id) && moves.containsKey(player2Id)) {
            Move player1Move = moves.get(player1Id);
            Move player2Move = moves.get(player2Id);
            
            // Calculate result
            RoundResult result = new RoundResult(session, player1Move, player2Move);
            
            // Clear moves for next round
            moves.clear();
            
            // Update game status if needed
            if (result.isGameOver()) {
                session.setStatus(GameStatus.FINISHED);
            }
            
            return Optional.of(result);
        }
        
        return Optional.empty();
    }
    
    // Get a game session by ID
    public Optional<GameSession> getSession(String sessionId) {
        return Optional.ofNullable(gameSessions.get(sessionId));
    }
    
    // Get a session directly without Optional (throws exception if not found)
    public GameSession getSessionRequired(String sessionId) {
        GameSession session = gameSessions.get(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Game session not found with ID: " + sessionId);
        }
        return session;
    }
    
    // Get all available games for joining
    public Map<String, GameSession> getAvailableGames() {
        return gameSessions.entrySet().stream()
                .filter(entry -> entry.getValue().canJoin())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    // Cleanup finished or abandoned games (could be scheduled with @Scheduled)
    public void cleanupSessions() {
        long currentTime = System.currentTimeMillis();
        long timeout = 30 * 60 * 1000; // 30 minutes
        
        gameSessions.entrySet().removeIf(entry -> {
            GameSession session = entry.getValue();
            return session.getStatus() == GameStatus.FINISHED || 
                   (currentTime - session.getCreatedAt() > timeout && session.getStatus() == GameStatus.WAITING);
        });
        
        // Also clean up the moves map
        sessionMoves.keySet().retainAll(gameSessions.keySet());
    }
}