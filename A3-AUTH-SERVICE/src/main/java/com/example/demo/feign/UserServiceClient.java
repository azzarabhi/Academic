package com.example.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.AuthUserResponse;

@FeignClient(
	    name = "A4-USER-SERVICE"
)
public interface UserServiceClient {

    @GetMapping("/users/cin/{cin}")
    AuthUserResponse getUserByCin(
            @PathVariable("cin") String cin);

}