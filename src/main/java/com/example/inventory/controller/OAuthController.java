package com.example.inventory.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/oauth")
@CrossOrigin(origins = "http://localhost:5173")
public class OAuthController {

    private final Dotenv dotenv = Dotenv.configure()
            .directory("src/main/resources")
            .load();

    private final String CLIENT_ID = dotenv.get("GOOGLE_CLIENT_ID");
    private final String CLIENT_SECRET = dotenv.get("GOOGLE_CLIENT_SECRET");
    private final String REDIRECT_URI = dotenv.get("GOOGLE_REDIRECT_URI");  // Optional for full OAuth flow

    @PostMapping("/google")
    public ResponseEntity<Map<String, String>> googleLogin(@RequestBody Map<String, String> request) {
        try {
            // ✅ Accept access_token from frontend
            String accessToken = request.get("access_token");

            // Step: Fetch user info using access token
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    userInfoRequest,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode profile = mapper.readTree(userInfoResponse.getBody());
            String email = profile.get("email").asText();
            String name = profile.get("name").asText();

            // ✅ (Optional) Save to agent table if needed

            Map<String, String> result = new HashMap<>();
            result.put("email", email);
            result.put("name", name);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "OAuth failed"));
        }
    }
}
