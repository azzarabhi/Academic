package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.dto.UserDTO;
import java.util.List;

@FeignClient(
    name = "A4-USER-SERVICE",
    configuration = FeignConfig.class
)
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable Long id);

    @GetMapping("/users")          // ← ZIDHA
    List<UserDTO> getAllUsers();
}