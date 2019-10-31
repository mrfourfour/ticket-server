package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.objectmapper.TicketMapper;
import com.mrfofo.ticket.security.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TicketRepository implements DynamoDbRepository<Ticket, String> {

    private final TicketMapper ticketMapper;
    private final DynamoDbAsyncClient client;
    private final AuthService authService;
    @Override
    public Flux<Ticket> findAll() {
        try {
            String userId = authService.getClaims().getUuid();
            CompletableFuture<QueryResponse> future = client.query(QueryRequest.builder()
                    .tableName("ticket")
                    .indexName("sortByDate")
                    .keyConditionExpression("PK = :pk and user_id = :user_id")
                    .expressionAttributeValues(Map.of(
                        ":pk", AttributeValue.builder().s("Ticket").build(),
                        ":user_id", AttributeValue.builder().s(userId).build())
                    )
                    .scanIndexForward(true)
                    .build());
    
            CompletableFuture<List<Ticket>> ticketListFuture = future
                    .thenApplyAsync(QueryResponse::items)
                    .thenApplyAsync(list -> list.parallelStream()
                        .map(ticketMapper::toObj)
                        .collect(Collectors.toList())
                    );
            return Mono.fromFuture(ticketListFuture).flatMapMany(Flux::fromIterable);
        } catch (ParseException pe) {
            return Flux.empty();
        }
        
    }

    @Override
    public Mono<Ticket> findById(String id) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("ticket")
                .key(Map.of(
                        "PK", AttributeValue.builder().s("Ticket").build(),
                        "SK", AttributeValue.builder().s(id).build())
                )
                .build();

        return Mono.fromFuture(client
                .getItem(getItemRequest)
                .thenApplyAsync(GetItemResponse::item)
                .thenApplyAsync(ticketMapper::toObj));
    }

    @Override
    public Mono<Ticket> save(Ticket ticket) {
        PutItemRequest putItemRequest = PutItemRequest.builder()
            .tableName("ticket")
            .item(ticketMapper.toMap(ticket))
            .build();

        return Mono.fromFuture(client.putItem(putItemRequest)
            .thenApplyAsync(PutItemResponse::attributes)
            .thenApplyAsync(ticketMapper::toObj));
    }

    @Override
    public Mono<Void> delete() {
        return null;
    }
}
