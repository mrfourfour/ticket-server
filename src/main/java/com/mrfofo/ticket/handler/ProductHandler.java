package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.repository.DynamoDbRepository;
import com.mrfofo.ticket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductRepository repository;

    public Mono<ServerResponse> findByCategory(ServerRequest serverRequest) {
        String category = serverRequest.pathVariable("category").toUpperCase();
        return repository.findByCategory(category).collectList().flatMap(products -> ServerResponse.ok().body(fromObject(Map.of("data", products))));
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return repository.findAll().collectList().flatMap(products -> ServerResponse.ok().body(fromObject(Map.of("data", products))));
    }

    public Mono<ServerResponse> findByCategoryAndId(ServerRequest serverRequest) {
        String category = serverRequest.pathVariable("category");
        String id = serverRequest.pathVariable("id");

        return repository.findByCategoryAndId(category, id).flatMap(product ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(product)));
    }
}
