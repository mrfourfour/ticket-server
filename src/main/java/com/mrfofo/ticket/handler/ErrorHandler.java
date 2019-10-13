package com.mrfofo.ticket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ErrorHandler {
    public Mono<ServerResponse> notFound(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromObject("잘못 치셨는디유"));
    }

    public Mono<ServerResponse> notEqual(final Throwable error) {
        return ServerResponse.badRequest().body(fromObject(error.getMessage()));
    }
}
