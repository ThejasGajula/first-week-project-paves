package com.rpsgame.controller;

import org.springframework.web.multipart.MultipartFile;

public class PredictRequestModel {
    private MultipartFile image;
    private String opponentMove; // rock / paper / scissors

    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }

    public String getOpponentMove() { return opponentMove; }
    public void setOpponentMove(String opponentMove) { this.opponentMove = opponentMove; }
}
