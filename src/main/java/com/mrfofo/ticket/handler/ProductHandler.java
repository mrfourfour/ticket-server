package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.repository.DynamoDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final DynamoDbRepository<Product, String> repository;

    public Mono<ServerResponse> findByCategory(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return repository.findAll().collectList().flatMap(products -> ServerResponse.ok().body(fromObject(Map.of("data", products))));
    }
}
