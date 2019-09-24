package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.objectmapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TicketRepository implements DynamoDbRepository<Ticket, String> {

    private final TicketMapper ticketMapper;
    private final DynamoDbAsyncClient client;

    @Override
    public Flux<Ticket> findAll() {
        CompletableFuture<ScanResponse> future = client.scan(ScanRequest.builder()
                .tableName("ticket")
                .scanFilter(Map.of(
                        "date",
                        Condition.builder()
                                .comparisonOperator(ComparisonOperator.LT)
                                .attributeValueList(AttributeValue.builder().s(LocalDateTime.now().toString()).build())
                                .build()
                ))
                .build());
        CompletableFuture<List<Ticket>> ticketListFuture = future
                .thenApplyAsync(ScanResponse::items)
                .thenApplyAsync(list -> list.parallelStream()
                    .map(ticketMapper::toTicket)
                    .collect(Collectors.toList())
                );
        return Mono.fromFuture(ticketListFuture).flatMapMany(Flux::fromIterable);
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
                .thenApplyAsync(ticketMapper::toTicket));
    }

    @Override
    public Mono<Ticket> save(Ticket ticket) {
        return null;
    }

    @Override
    public Mono<Void> delete() {
        return null;
    }
}
