package com.rpsgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PredictController{

    @GetMapping("/greet")
    public String predict(){
        return "Hello world";
    }
}