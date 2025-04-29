package com.rpsgame.Model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class PredictRequestModel {
    private MultipartFile image;
    private String opponentMove; // rock / paper / scissors

}
