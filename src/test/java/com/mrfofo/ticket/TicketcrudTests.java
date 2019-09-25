package com.mrfofo.ticket;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.objectmapper.ProductMapper;
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
    ProductMapper productMapper;
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
                assertThat(ticketMapper.toObj(resp.item()), is(ticket));
            });

            item.join();
        });
        future.join();
    }

    @Test
    @Ignore
    public void createAndGetProduct() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("할로우상품")
                .sellerId("Seller-1")
                .image("https://avatars0.githubusercontent.com/u/54847050?s=200&v=4")
                .category("mainmain")
                .subCategory("subsub")
                .info("설명충은 사양아에요.")
                .area(Product.ProductArea.JEJU.getValue())
                .price(30000L)
                .option("dummy")
                .build();

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("ticket")
                .item(productMapper.toMap(product))
                .build();

        client.putItem(putItemRequest)
        .whenComplete((res, err) -> {
            if(res == null)
                err.printStackTrace();
        })
        .thenAcceptAsync(res -> {
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName("ticket")
                    .key(Map.of(
                            "PK", AttributeValue.builder().s("Product").build(),
                            "SK", AttributeValue.builder().s(product.getId()).build()
                    ))
                    .build();

            client.getItem(getItemRequest)
                    .whenComplete((resp, err) -> {
                        if(resp == null)
                            err.printStackTrace();
                    })
                    .thenAcceptAsync(resp -> {
                        assertThat(productMapper.toObj(resp.item()), is(product));
                    }).join();
        }).join();


    }
}
