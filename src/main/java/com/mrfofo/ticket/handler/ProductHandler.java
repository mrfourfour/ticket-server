package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.payload.ReviewPayLoad;
import com.mrfofo.ticket.repository.ProductRepository;
import com.mrfofo.ticket.security.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {

    private final ProductRepository repository;
    private final AuthService authService;

    public Mono<ServerResponse> findByAreaAndCategory(ServerRequest serverRequest) {
        String area = serverRequest.pathVariable("area").toUpperCase();
        String category = serverRequest.pathVariable("category").toUpperCase();
        return repository.findByAreaAndCategory(area, category).collectList().flatMap(products -> ServerResponse.ok().body(fromObject(Map.of("data", products))));
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

    public Mono<ServerResponse> saveReview(ServerRequest serverRequest) {
        try {
            String uuid = authService.getClaims().getUuid();
            return serverRequest.bodyToMono(ReviewPayLoad.class)
                    .flatMap(reviewPayLoad -> repository.findById(reviewPayLoad.getProductId())
                            .flatMap(product -> {
                                Product.Review review = reviewPayLoad.getReview();
                                boolean isContain = product.getReviews().contains(review);
                                if(isContain) {
                                    return Mono.just(false);
                                } else {
                                    review.setUserId(uuid);
                                    product.getReviews().add(review);
                                    repository.update(product);
                                    return Mono.just(true);   
                                }
                            }))
                            .flatMap(result -> result
                                    ? ServerResponse.ok().body(BodyInserters.fromObject(Map.of("data", true)))
                                    : ServerResponse.badRequest().body(BodyInserters.fromObject(Map.of("data", false))));
            
            // Mono<Product> productMono = repository.findById(reviewPayLoadMono.flat);
            // TODO 검증위한 테스트 코드 필요.
            // return serverRequest.bodyToMono(ReviewPayLoad.class)
            //         .flatMap(reviewPayLoad -> {
            //                 boolean isContain = repository.findById(reviewPayLoad.getProductId())
            //                         .filter(product -> product.getReviews().contains(reviewPayLoad.getReview()));
            //                 if(isContain)
            //         })
                
                    // .flatMap(product -> Mono.just(product.getReviews().contains(reviewPayLoad.getReview())))
                    // .flatMap(result -> result
                    //         ? ServerResponse.ok().body(BodyInserters.fromObject(Map.of("data", true)))
                    //         : ServerResponse.badRequest().body(BodyInserters.fromObject(Map.of("data", false))));
        } catch (ParseException e) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).body(BodyInserters.fromObject(e.getMessage()));
        }
        
    }

    // public Mono<ServerResponse> save(ServerRequest serverRequest) {
    //     return serverRequest.bodyToMono(Product.class)
    //     .doOnNext(product -> {
    //         log.info(product.toString());
    //         repository.save(product);
    //     })
    //     .doOnError(NotEqualException.class, errorHandler::notEqual)
    //     .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(product)))
    //     .switchIfEmpty(ServerResponse.notFound().build());
    // }
}
