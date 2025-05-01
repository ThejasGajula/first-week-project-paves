package com.rpsgame.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerSetupRequest {
    
    private String name = "Player"; // default name
    private String handPreference = "right"; // left / right
    private Integer rounds = 3; // default rounds

   

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHandPreference() { return handPreference; }
    public void setHandPreference(String handPreference) { this.handPreference = handPreference; }

    public Integer getRounds() { return rounds; }
    public void setRounds(Integer rounds) { this.rounds = rounds; }
}