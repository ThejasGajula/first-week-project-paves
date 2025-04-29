package com.rpsgame.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerSetupRequest {
    
    private String name;
    private String handPreference; // left / right
    private Integer rounds;

   

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHandPreference() { return handPreference; }
    public void setHandPreference(String handPreference) { this.handPreference = handPreference; }

    public Integer getRounds() { return rounds; }
    public void setRounds(Integer rounds) { this.rounds = rounds; }
}