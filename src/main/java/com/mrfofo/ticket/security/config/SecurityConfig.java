package com.mrfofo.ticket.security.config;

import com.mrfofo.ticket.security.repository.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                    .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    }))
                    .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                        swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    }))
                    .and()
                .authorizeExchange()
                    .pathMatchers("/", "/login/**", "/auth/login", "/auth/token").permitAll()
                    .anyExchange().authenticated()
                    .and()
                .securityContextRepository(securityContextRepository)
                .authenticationManager(authenticationManager)
                .build();
    }
}
