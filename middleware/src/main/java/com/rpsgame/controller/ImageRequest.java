package com.rpsgame.controller;

import lombok.Data;

@Data
public class ImageRequest{

    private String image;
    private String opponentMove;
    public ImageRequest(){

    }
    public String getImage(){
        return image;
    }
    public void setImage(String image)
    {
        this.image=image;
    }
}