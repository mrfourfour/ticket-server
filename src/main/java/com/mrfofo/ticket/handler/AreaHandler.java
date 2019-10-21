package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AreaHandler {

    public Mono<ServerResponse> findArea(ServerRequest serverRequest) {
        return Flux.just(Product.ProductArea.values()).collectList().flatMap(productAreas -> ServerResponse.ok().body(BodyInserters.fromObject(productAreas)));
    }
}
