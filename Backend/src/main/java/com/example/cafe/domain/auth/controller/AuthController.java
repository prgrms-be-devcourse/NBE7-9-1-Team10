package com.example.cafe.domain.auth.controller;

import com.example.cafe.domain.auth.dto.LoginRequest;
import com.example.cafe.domain.auth.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String email = request.getEmail();

        if ("admin@email.com".equals(email)) {
            return ResponseEntity.ok(new LoginResponse("admin", email));
        } else {
            return ResponseEntity.ok(new LoginResponse("user", email));
        }
    }
}
