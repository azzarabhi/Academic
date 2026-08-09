package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dto.NewStudentRequestDTO;


@FeignClient(name = "A6-NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notifications/new-student")
    void notifyNewStudent(@RequestBody NewStudentRequestDTO req);
}