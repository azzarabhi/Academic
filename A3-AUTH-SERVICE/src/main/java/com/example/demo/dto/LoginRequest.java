package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Le CIN est obligatoire")
    private String cin;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    private Boolean rememberMe = false;

    public LoginRequest() {}

    public LoginRequest(String cin, String password, Boolean rememberMe) {
        this.cin = cin;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}