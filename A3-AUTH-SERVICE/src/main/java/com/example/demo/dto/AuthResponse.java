package com.example.demo.dto;

public class AuthResponse {

    private String token;
    private String role;
    private Long userId;

    public AuthResponse() {}

    public AuthResponse(String token, String role, Long userId) {
        this.token = token;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }
}