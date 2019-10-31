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
public class ProductRepositoryImpl implements ProductRepository {

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
    @Override
    public Flux<Product> findByAreaAndCategory(String area, String category) {
        CompletableFuture<QueryResponse> future = client.query(QueryRequest.builder()
                .tableName("ticket")
                .indexName("sortByDate")
                .keyConditionExpression("PK = :product")
                .filterExpression("area = :area and category = :category")
                .expressionAttributeValues(Map.of(
                        ":product", AttributeValue.builder().s("Product").build(),
                        ":area", AttributeValue.builder().s(area).build(),
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
    @Override
    public Mono<Product> save(Product product) {
        if(product.getSubCategory().getParentProductCategory() == product.getCategory())
            return Mono.error(new NotEqualException());

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

    @Override
    public Flux<Product> findPopularItemEachCategory() {
        return null;
    }

    @Override
    public Mono<Product> update(Product t) {
        Map<String, AttributeValueUpdate> reviewUpdate = Map.of(
                "reviews",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().l(t.getReviews()
                                .parallelStream()
                                .map(review -> AttributeValue.builder().m(Map.ofEntries(
                                        Map.entry("user_id", AttributeValue.builder().s(review.getUserId()).build()),
                                        Map.entry("title", AttributeValue.builder().s(review.getTitle()).build()),
                                        Map.entry("description", AttributeValue.builder().s(review.getDescription()).build()),
                                        Map.entry("rate", AttributeValue.builder().n(String.valueOf(review.getRate())).build())
                                )).build()).collect(Collectors.toSet())
                        ).build())
                        .build()
        .build();
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
            .tableName("ticket")
            .key(Map.of(
                "PK", AttributeValue.builder().s("Product").build(),
                "SK", AttributeValue.builder().s(t.getId()).build()
            ))
            .attributeUpdates(reviewUpdate)
            .build();
        
        return Mono.fromFuture(client
            .updateItem(updateItemRequest)
            .thenApplyAsync(UpdateItemResponse::attributes)
            .thenApplyAsync(productMapper::toObj));
    }
}
