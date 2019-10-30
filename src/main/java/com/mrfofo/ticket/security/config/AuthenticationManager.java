package com.mrfofo.ticket.security.config;

import com.mrfofo.ticket.security.cognito.CognitoAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof CognitoAuthenticationToken) {
            authentication.setAuthenticated(true);
            return Mono.just(authentication);
        }
        return Mono.empty();
    }
}