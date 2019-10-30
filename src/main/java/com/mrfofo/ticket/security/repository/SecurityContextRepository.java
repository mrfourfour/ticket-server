package com.mrfofo.ticket.security.repository;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import com.mrfofo.ticket.security.cognito.CognitoAuthenticationToken;
import com.mrfofo.ticket.security.config.AuthenticationManager;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final ConfigurableJWTProcessor<com.nimbusds.jose.proc.SecurityContext> processor;
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            String token = extractToken(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            CognitoAuthenticationToken authenticationToken = extractAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return authenticationManager.authenticate(authenticationToken).map(SecurityContextImpl::new);
        } catch (AccessDeniedException e) {
            log.error("Access denied: " + (!e.getMessage().isBlank() ? e.getMessage() : "No message"));
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }
    }

    private CognitoAuthenticationToken extractAuthentication(String token) throws AccessDeniedException {
        if(token == null) return null;
        try {
            JWTClaimsSet claims = processor.process(token, null);
            return CognitoAuthenticationToken.builder()
                    .token(token)
                    .details(claims)
                    .build();
        } catch (Exception e) {
            throw new AccessDeniedException(e.getClass().getSimpleName());
        }
    }

    private String extractToken(String header) {
        return Optional.ofNullable(header)
                .filter(s -> s.startsWith("Bearer "))
                .map(s -> s.split("Bearer "))
                .map(headers -> headers[1])
                .orElse(null);
    }


}