package com.example.inventory.controller;

import com.example.inventory.service.InventoryUserService;
import com.example.inventory.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/inventory")
public class InventoryLoginController {

    @Autowired
    private InventoryUserService service;

    @Autowired
    private JwtUtil jwtUtil; // ✅ Inject JWT utility

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = service.login(request.getUsername(), request.getPassword());

        if (success) {
            String token = jwtUtil.generateToken(request.getUsername()); // ✅ Generate token
            return ResponseEntity.ok().body(new LoginResponse(token));   // ✅ Return as JSON
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // ✅ Inner class to return the token
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}