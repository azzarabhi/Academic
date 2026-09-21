package com.example.demo.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorsErrorFilter implements GlobalFilter, Ordered {

    // ✅ Origine autorisée
    private static final String ALLOWED_ORIGIN = "http://localhost:4200";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String origin = request.getHeaders().getOrigin();
        HttpMethod method = request.getMethod();

        System.out.println("🌐 CORS FILTER — method=" + method + " origin=" + origin);

        // ✅ Étape 1 : Preflight OPTIONS — répondre immédiatement
        if (HttpMethod.OPTIONS.equals(method)) {
            System.out.println("✅ OPTIONS — réponse immédiate CORS");

            HttpHeaders headers = response.getHeaders();
            headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
            headers.set("Access-Control-Allow-Credentials", "true");
            headers.set("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD");
            headers.set("Access-Control-Allow-Headers",
                    "Authorization, Content-Type, Accept, X-Requested-With, " +
                    "Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
            headers.set("Access-Control-Expose-Headers",
                    "Authorization, Content-Disposition, Content-Type, Content-Length");
            headers.set("Access-Control-Max-Age", "3600");

            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // ✅ Étape 2 : Ajouter headers CORS sur toutes les autres requêtes
        return chain.filter(exchange).doFinally(signalType -> {
            HttpHeaders headers = response.getHeaders();

            // Éviter duplication
            if (!headers.containsKey("Access-Control-Allow-Origin")) {
                headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                headers.set("Access-Control-Allow-Credentials", "true");
                headers.set("Access-Control-Expose-Headers",
                        "Authorization, Content-Disposition, Content-Type, Content-Length");
            }
        });
    }

    @Override
    public int getOrder() {
        // ✅ Le plus haut — avant tout
        return Ordered.HIGHEST_PRECEDENCE;
    }
}