package com.rpsgame.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import com.rpsgame.Model.GameResult;
import com.rpsgame.Model.GameRoom;
import com.rpsgame.Model.GestureRequest;
import com.rpsgame.Model.PlayerSetupRequest;
import com.rpsgame.Model.PredictResponse;
import com.rpsgame.Model.ScoreResponse;
import com.rpsgame.Service.GameService;
import com.rpsgame.Service.MatchmakingService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api")
public class GameController {

     private final MatchmakingService matchmakingService;
     private final SimpMessagingTemplate messagingTemplate;

    public GameController(SimpMessagingTemplate messagingTemplate,MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
        this.messagingTemplate = messagingTemplate;
    }


    @Autowired
    private GameService gameService;

    @PostMapping("/setup")
    public String setupPlayer(@RequestBody PlayerSetupRequest request) {
        gameService.setupPlayer(request);
        return "Player setup successful!";
    }

    @GetMapping("/score")
    public ScoreResponse getScore() {
        return gameService.getScore();
    }

    @PostMapping("/submit")
    public PredictResponse predict(@RequestBody Map<String, String> request) {
        return gameService.predict(request.get("playerMove"), request.get("opponentMove"));
    }

    @GetMapping("/restart")
    public ResponseEntity restart() {
        gameService.restartGame();
        return ResponseEntity.ok("Game restarted successfully!");
    }
    
@MessageMapping("/ready")
public void handleReady(@Header("simpSessionId") String sessionId) {
    String roomId = matchmakingService.getRoomId(sessionId);
    GameRoom room = matchmakingService.getRoom(roomId);
    if (room == null) return;

    room.markReady(sessionId);
    if (room.allReady()) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, "Both players are ready. Submit your move!");
    }
}

@MessageMapping("/move")
public void handleMove(@Payload GestureRequest gestureRequest,
                       @Header("simpSessionId") String sessionId) {

    String roomId = matchmakingService.getRoomId(sessionId);
    GameRoom room = matchmakingService.getRoom(roomId);
    if (room == null) return;

    room.submitMove(sessionId, gestureRequest.getMove());

    if (room.allMovesSubmitted()) {
        room.computeResult();
        GameResult result = new GameResult(
            room.getWinnerName(),
            room.getPlayerNames(),
            room.getWinnerName().equals("Draw") ? "It's a draw!" : room.getWinnerName() + " wins!"
        );
        messagingTemplate.convertAndSend("/topic/room/" + roomId, result);
        room.reset(); // Optional: reset for next round
    }
}

    
}
