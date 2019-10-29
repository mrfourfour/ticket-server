package com.mrfofo.ticket.security.filter;

import com.mrfofo.ticket.security.cognito.CognitoAuthenticationToken;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Slf4j
public class AuthFilter extends AuthenticationWebFilter {
    private ConfigurableJWTProcessor<SecurityContext> processor;
    public AuthFilter(ConfigurableJWTProcessor<SecurityContext> processor, ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.processor = processor;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            String token = extractToken(exchange.getRequest().getHeaders().getFirst("Authorization"));
            CognitoAuthenticationToken authentication = extractAuthentiction(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("Access denied: " + (!e.getMessage().isBlank() ? e.getMessage() : "No message"));
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(401));
            return Mono.empty();
        }
    }

    private String extractToken(String header) {
        return Optional.ofNullable(header)
                .filter(s -> s.startsWith("Bearer "))
                .map(s -> s.split("Bearer "))
                .map(headers -> headers[1])
                .orElse(null);
    }

    private CognitoAuthenticationToken extractAuthentiction(String token) throws AccessDeniedException {
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
}
