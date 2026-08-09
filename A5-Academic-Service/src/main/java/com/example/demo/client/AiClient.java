package com.example.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.demo.dto.ia.StudentAnalysisRequestDTO;
import com.example.demo.dto.ia.StudentAnalysisResponseDTO;

@Component
public class AiClient {

    private final WebClient webClient;

    public AiClient(@Value("${ai.service.url}") String aiServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(aiServiceUrl).build();
    }

    public StudentAnalysisResponseDTO analyzeStudent(StudentAnalysisRequestDTO req) {
        return webClient.post()
                .uri("/ai/analyze/student")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(StudentAnalysisResponseDTO.class)
                .block();
    }
}