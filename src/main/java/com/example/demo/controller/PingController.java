package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://myproject-1-vf3w.onrender.com"
        }
)
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }
}