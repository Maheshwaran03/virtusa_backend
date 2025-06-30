package com.example.inventory.controller;

import com.example.inventory.model.AgentCredential;
import com.example.inventory.repository.DeliveryRepository;
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

    @Autowired
    private DeliveryRepository deliveryRepository;

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

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam String email) {
        AgentCredential agent = service.getByEmail(email);
        if (agent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Agent not found"));
        }

        int count = deliveryRepository.countByAgentEmail(agent.getEmail());

        Map<String, Object> profile = new HashMap<>();
        profile.put("name", agent.getName());
        profile.put("email", agent.getEmail());
        profile.put("phone", agent.getMobileNumber());
        profile.put("joined", agent.getJoinedOn());
        profile.put("deliveriesMade", count);

        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam String email,
            @RequestBody Map<String, String> updates
    ) {
        AgentCredential agent = service.getByEmail(email);
        if (agent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Agent not found"));
        }

        if (updates.containsKey("name")) {
            agent.setName(updates.get("name"));
        }
        if (updates.containsKey("mobileNumber")) {
            agent.setMobileNumber(updates.get("mobileNumber"));
        }

        AgentCredential updated = service.save(agent);
        return ResponseEntity.ok(Map.of("message", "Profile updated", "agent", updated));
    }

    // ✅ Return only needed agent info (name, email) to frontend
    @GetMapping("/agents")
    public ResponseEntity<?> getAllAgents() {
        List<AgentCredential> agents = service.getAllAgents();

        List<Map<String, String>> result = new ArrayList<>();
        for (AgentCredential a : agents) {
            Map<String, String> map = new HashMap<>();
            map.put("name", a.getName());
            map.put("email", a.getEmail());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}
