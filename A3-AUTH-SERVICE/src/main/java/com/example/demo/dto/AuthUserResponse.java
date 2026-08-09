package com.example.demo.dto;
import java.util.Set;

public class AuthUserResponse {

    private Long id;
    private String cin;
    private String password;
    private Set<String> roles;
    private boolean enabled;  // ← ZIDHA

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public boolean isEnabled() { return enabled; }       // ← ZIDHA
    public void setEnabled(boolean enabled) { this.enabled = enabled; }  // ← ZIDHA

    // Constructor complet — zid enabled
    public AuthUserResponse(Long id, String cin, String password, Set<String> roles, boolean enabled) {
        this.id = id;
        this.cin = cin;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;  // ← ZIDHA
    }

    public AuthUserResponse() {}
}