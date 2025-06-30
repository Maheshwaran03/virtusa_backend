package com.example.inventory.service;

import com.example.inventory.model.AgentCredential;
import com.example.inventory.repository.AgentCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AgentCredentialService {

    @Autowired
    private AgentCredentialRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> register(AgentCredential agent) {
        if (repo.existsByEmail(agent.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        AgentCredential saved = repo.save(agent);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registered");
        response.put("email", saved.getEmail());
        response.put("name", saved.getName());
        return response;
    }

    public AgentCredential getByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}