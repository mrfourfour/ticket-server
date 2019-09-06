package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.mapper.TestMapper;
import com.mrfofo.ticket.model.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DynamoTestRepository implements TestRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    @Override
    public Mono<Test> findById(Long id) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .key(Map.of("id", AttributeValue.builder().s(String.valueOf(id)).build()))
                .tableName("test")
                .build();
        return Mono.fromCompletionStage(dynamoDbAsyncClient.getItem(getItemRequest))
                .map(getItemResponse -> TestMapper.getInstance().fromMap(id, getItemResponse.item()));
    }

    @Override
    public Mono<Test> save(Test test) {
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("test")
                .item(TestMapper.getInstance().toMap(test))
                .build();
        return Mono.fromCompletionStage(dynamoDbAsyncClient.putItem(putItemRequest))
                .flatMap(putItemResponse -> findById(test.getId()));
    }

    @Override
    public Mono<Boolean> deleteById(Long aLong) {
        return null;
    }

    @Override
    public Flux<Test> findAll() {
        return null;
    }
}
