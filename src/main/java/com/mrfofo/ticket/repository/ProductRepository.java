package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.exception.NotEqualException;
import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.objectmapper.DynamoDbMapper;
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
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("ticket")
                .key(Map.of(
                        "PK", AttributeValue.builder().s("Product").build(),
                        "SK", AttributeValue.builder().s(id).build()))
                .build();
        return Mono.fromFuture(client
                .getItem(getItemRequest)
                .thenApplyAsync(GetItemResponse::item)
                .thenApplyAsync(productMapper::toObj));
    }

    public Flux<Product> findByCategory(String category) {
        CompletableFuture<QueryResponse> future = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("sortByDate")
                .keyConditionExpression("PK = :product")
                .filterExpression("category = :category")
                .expressionAttributeValues(Map.of(
                        ":product", AttributeValue.builder().s("Product").build(),
                        ":category", AttributeValue.builder().s(category).build()
                ))
                .build());

        CompletableFuture<List<Product>> productListFuture = future
                .thenApplyAsync(QueryResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                    .map(productMapper::toObj)
                    .collect(Collectors.toList())
                );
        return Mono.fromFuture(productListFuture).flatMapMany(Flux::fromIterable);
    }

    public Mono<Product> findByCategoryAndId(String category, String id) {

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("ticket")
                .key(Map.of(
                        "PK", AttributeValue.builder().s("Product").build(),
                        "category", AttributeValue.builder().s(category).build(),
                        "SK", AttributeValue.builder().s(id).build()))
                .build();
        return Mono.fromFuture(client
                .getItem(getItemRequest)
                .thenApplyAsync(GetItemResponse::item)
                .thenApplyAsync(productMapper::toObj));
    }

    @Override
    public Mono<Product> save(Product product) {
		// // TODO 카테고리 다르면 NotEqualException 처리하기!
		// if(!product.getCategory().equals(product.getSubCategory().getParentProductCategory())) {
		// 	throw new NotEqualException();
		// }

        PutItemRequest putItemRequest = PutItemRequest.builder()
			.tableName("ticket")
			.item(productMapper.toMap(product))
			.build();

        return Mono.fromFuture(client
			.putItem(putItemRequest)
			.thenApplyAsync(PutItemResponse::attributes)
			.thenApplyAsync(productMapper::toObj));
		
    }

    @Override
    public Mono<Void> delete() {
        return null;
    }
}
