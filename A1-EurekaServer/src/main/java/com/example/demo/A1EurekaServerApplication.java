package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class A1EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(A1EurekaServerApplication.class, args);
	}

}
