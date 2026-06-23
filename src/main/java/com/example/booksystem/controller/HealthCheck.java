package com.example.booksystem.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthCheck {

    @GetMapping("health")
    public Map<String, Object> healthCheck() {
        return Map.of(
                "status", "UP",
                "message", "Book system in running...",
                "timestamp", LocalDateTime.now()
        );
    }
}
