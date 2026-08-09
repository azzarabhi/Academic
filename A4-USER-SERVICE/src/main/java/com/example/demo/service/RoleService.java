package com.example.demo.service;


import org.springframework.stereotype.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.RoleUpdateRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;

@Service
public class RoleService {

    private final UserRepository userRepository;

    public RoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User changeRole(Long id,
                           RoleUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow();

        user.setRole(request.getRole());

        return userRepository.save(user);
    }
}