package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients
@SpringBootApplication
public class A3AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(A3AuthServiceApplication.class, args);
	}

}
