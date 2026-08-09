package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.AuthUserResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.feign.UserServiceClient;

@Service
public class AuthService {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserServiceClient userServiceClient,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userServiceClient = userServiceClient;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {

        System.out.println("SEARCH USER...");

        AuthUserResponse user =
                userServiceClient.getUserByCin(request.getCin());

        System.out.println("USER = " + user);

     // if (!user.isEnabled()) {
//      throw new RuntimeException("Compte non activé. Vérifiez votre email.");
 // }

        System.out.println("DB PASSWORD = " + user.getPassword());
        System.out.println("REQUEST PASSWORD = " + request.getPassword());

        boolean valid =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        System.out.println("PASSWORD VALID = " + valid);

        if (!valid) {
            throw new RuntimeException("Invalid credentials");
        }

        String role = user.getRoles()
                .stream()
                .findFirst()
                .orElse("USER");

        String token =
                jwtService.generateToken(
                        user.getCin(),
                        role);

        return new AuthResponse(
                token,
                role,
                user.getId()
        );
    }
    
    
    }