package com.example.inventory.controller;

import com.example.inventory.model.AgentCredential;
import com.example.inventory.service.AgentCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "http://localhost:5173")
public class AgentCredentialController {

    @Autowired
    private AgentCredentialService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AgentCredential agent) {
        try {
            return ResponseEntity.ok(service.register(agent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        AgentCredential agent = service.getByEmail(email);
        if (agent == null || !service.checkPassword(password, agent.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }

        return ResponseEntity.ok(Map.of(
                "email", agent.getEmail(),
                "name", agent.getName()
        ));
    }
}