package com.mrfofo.ticket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {
    public Mono<ServerResponse> findByCategory(ServerRequest serverRequest) {
        return null;
    }
}
