package com.example.demo.Repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
extends JpaRepository<User, Long> {

Optional<User> findByCin(String cin);
Optional<User> findByEmail(String email);
Optional<User>findByVerificationToken(String token);
Optional<User> findByResetToken(String token);
}