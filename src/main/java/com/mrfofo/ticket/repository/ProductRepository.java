package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.objectmapper.DynamoDbMapper;
import com.mrfofo.ticket.objectmapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepository implements DynamoDbRepository<Product, String> {

    private final DynamoDbMapper<Product> productMapper;
    private final DynamoDbAsyncClient client;

    @Override
    public Flux<Product> findAll() {
        CompletableFuture<QueryResponse> future = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("sortByDate")
                .keyConditionExpression("PK = :pk")
                .expressionAttributeValues(Map.of(":pk", AttributeValue.builder().s("Product").build()))
                .scanIndexForward(false)
                .build());

        CompletableFuture<List<Product>> productListFuture = future
                .thenApplyAsync(QueryResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                    .map(productMapper::toObj)
                    .collect(Collectors.toList())
                );

        return Mono.fromFuture(productListFuture).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Product> findById(String id) {
        return null;
    }

    @Override
    public Mono<Product> save(Product product) {
        return null;
    }

    @Override
    public Mono<Void> delete() {
        return null;
    }
}
