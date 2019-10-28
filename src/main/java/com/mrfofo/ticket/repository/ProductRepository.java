package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.model.Product;
import reactor.core.publisher.Flux;

public interface ProductRepository extends DynamoDbRepository<Product, String> {
    Flux<Product> findByAreaAndCategory(String area, String category);
    Flux<Product> findPopularItemEachCategory();
}
