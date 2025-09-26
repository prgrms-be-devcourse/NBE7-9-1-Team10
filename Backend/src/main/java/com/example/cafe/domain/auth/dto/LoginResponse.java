package com.example.cafe.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private String role;
    private String email;

    public LoginResponse(String role, String email) {
        this.role = role;
        this.email = email;
    }
}
