package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.exception.NotEqualException;
import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {

    private final ProductRepository repository;
    private final ErrorHandler errorHandler;

    public Mono<ServerResponse> findByCategory(ServerRequest serverRequest) {
        String category = serverRequest.pathVariable("category").toUpperCase();
        return repository.findByCategory(category).collectList().flatMap(products -> ServerResponse.ok().body(fromObject(Map.of("data", products))));
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return repository.findAll().collectList().flatMap(products -> ServerResponse.ok().body(fromObject(Map.of("data", products))));
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        log.info(id);
        return repository.findById(id).flatMap(product ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(Map.of("data", product))));
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
        .doOnNext(product -> {
            log.info(product.toString());
            //repository.save(product);
        })
        .doOnError(NotEqualException.class, errorHandler::notEqual)
        .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(product)))
        
        .switchIfEmpty(ServerResponse.notFound().build());
    }
}
