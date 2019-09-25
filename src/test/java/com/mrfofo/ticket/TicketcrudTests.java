package com.mrfofo.ticket;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.objectmapper.TicketMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketcrudTests {
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    DynamoDbAsyncClient client;

    @Test
    @Ignore
    public void createAndGetTicketTest() {
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID().toString())
                .productId("Product-1")
                .totalPrice(30000L)
                .amount(3)
                .qrData("guegue")
                .date(LocalDateTime.now().toString())
                .status(Ticket.TicketStatus.NOT_USED.getValue())
                .build();
        PutItemRequest request = PutItemRequest.builder()
                .tableName("ticket")
                .item(ticketMapper.toMap(ticket))
                .build();

        CompletableFuture<PutItemResponse> future = client.putItem(request);
        future.whenComplete((res, err) -> {
            if (res == null)
                err.printStackTrace();
        }).thenAcceptAsync(res -> {
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName("ticket")
                    .key(
                            Map.of(
                                    "PK", AttributeValue.builder().s("Ticket").build(),
                                    "SK", AttributeValue.builder().s(ticket.getId()).build()
                            )
                    )
                    .build();
            CompletableFuture<GetItemResponse> item = client.getItem(getItemRequest);
            item.whenComplete((resp, err) -> {
                if (resp == null)
                    err.printStackTrace();
            }).thenAcceptAsync(resp -> {
                assertThat(ticketMapper.toTicket(resp.item()), is(ticket));
            });

            item.join();
        });
        future.join();
    }
}
