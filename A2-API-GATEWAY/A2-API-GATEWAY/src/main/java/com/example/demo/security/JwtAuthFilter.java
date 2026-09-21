package com.example.demo.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "GET";

        System.out.println("➡️  PATH   = " + path);
        System.out.println("➡️  METHOD = " + method);

        // ✅ OPTIONS — laisser passer (déjà géré par CorsErrorFilter)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("✅ OPTIONS — laisser passer JWT");
            return chain.filter(exchange);
        }

        // ✅ Routes publiques
        if (
                path.contains("/auth/")
             || path.contains("/users/register")
             || path.contains("/users/verify")
             || path.contains("/users/cin/")
             || path.contains("/users/forgot-password")
             || path.contains("/users/reset-password")
             || path.contains("/uploads/")
             || path.startsWith("/notifications/")
        ) {
            System.out.println("✅ Route publique — laisser passer");
            return chain.filter(exchange);
        }

        // ✅ Vérifier token JWT
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        System.out.println("🔑 AUTH HEADER = " + (authHeader != null ? "présent" : "absent"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Token manquant — 401");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        System.out.println("✅ Token OK — vers " + path);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // ✅ Après CorsErrorFilter
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}