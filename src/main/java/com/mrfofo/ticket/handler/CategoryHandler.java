package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Product.ProductCategory;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CategoryHandler {
    public Mono<ServerResponse> findCategory(ServerRequest request) {
        return Flux.just(Product.ProductCategory.values()).collectList().flatMap(category -> ServerResponse.ok().body(BodyInserters.fromObject(category)));
    }
}