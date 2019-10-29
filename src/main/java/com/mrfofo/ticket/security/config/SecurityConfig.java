package com.mrfofo.ticket.security.config;

import com.mrfofo.ticket.security.filter.AuthFilter;
import com.mrfofo.ticket.security.manager.AuthenticationManager;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
// @EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ConfigurableJWTProcessor<SecurityContext> processor;
    private final AuthenticationManager authenticationManager;
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .csrf()
                    .disable()
                    .headers()
                    .frameOptions().disable()
                    .cache().disable()
                .and()
                    .authorizeExchange()
                    .pathMatchers("/", "/login/**", "/auth/login", "/auth/token").permitAll()
                    .anyExchange().authenticated()
                    .and()
                .addFilterAt(new AuthFilter(processor, authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                // .authenticationManager(authenticationManager)
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .build();
    }
}
