package com.mrfofo.ticket.handler;

import java.text.ParseException;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.security.service.AuthService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CognitoAuthHandler {
    @Value("${cognito.endpoints.authorize}")
    private String authorizeUri;

    private final AuthService authService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return ServerResponse.status(HttpStatus.SEE_OTHER).header(HttpHeaders.LOCATION, authorizeUri).build();
    }

    public Mono<ServerResponse> token(ServerRequest request) {
        String code = request.queryParam("code").orElseThrow(IllegalStateException::new);
        return ServerResponse.ok().body(BodyInserters.fromObject(authService.getToken(code)));
    }

    public Mono<ServerResponse> getCurrentUser(ServerRequest request) {
        try {
            return ServerResponse.ok().body(BodyInserters.fromObject(authService.getClaims()));
        } catch (ParseException e) {
            e.printStackTrace();
            return ServerResponse.status(401).build();
        }
    }
}
