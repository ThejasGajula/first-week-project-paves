package com.rpsgame.Model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Room {
    private String roomId;
    private String roomName;
    private int maxPlayers;
 private Map<String, Player> players = new HashMap<>(); // Player ID mapped to Player object

    public void togglePlayerReady(String playerId) {
        Player player = players.get(playerId);
        if (player != null) {
            player.setReady(!player.isReady());
        }
    
   
    

   
   
    }
}
