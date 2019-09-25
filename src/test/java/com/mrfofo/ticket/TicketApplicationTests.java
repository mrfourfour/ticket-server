package com.mrfofo.ticket;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Ticket;
import io.netty.util.concurrent.CompleteFuture;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Test
    @Ignore
    public void getTicketTest() {
        Ticket ticket = Ticket.builder()
                .id("Ticket-2")
                .productId("Product-9")
                .status(Ticket.TicketStatus.NOT_USED.getValue())
                .date("2019-01-13")
                .qrData("sdfsdf")
                .amount(5)
                .totalPrice(27000L)
                .build();

        webTestClient.get().uri("/api/ticket/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ticket.class)
                .isEqualTo(ticket);
    }

    @Test
    @Ignore
    public void createTableTest() {
        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder().attributeName("id").attributeType(ScalarAttributeType.S).build(),
                        AttributeDefinition.builder().attributeName("mentionId").attributeType(ScalarAttributeType.N).build(),
                        AttributeDefinition.builder().attributeName("createdAt").attributeType(ScalarAttributeType.S).build()
                )
                .tableName("Comment")
                .keySchema(
                        KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName("byMentionId")
                                .keySchema(
                                        KeySchemaElement.builder().attributeName("mentionId").keyType(KeyType.HASH).build(),
                                        KeySchemaElement.builder().attributeName("createdAt").keyType(KeyType.RANGE).build()
                                )
                                .projection(
                                        Projection.builder().projectionType(ProjectionType.ALL).build()
                                )
                                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
                                .build()
                )
                .build();

        CompletableFuture<CreateTableResponse> table = dynamoDbAsyncClient.createTable(createTableRequest);
        table.whenComplete((res, err) -> {
            try {
                CreateTableResponse request = Optional.ofNullable(res).orElseThrow(IllegalStateException::new);
                System.out.println(request.toString());
            } catch(IllegalStateException e) {
                err.printStackTrace();
            } finally {
                dynamoDbAsyncClient.close();
            }
        });

        table.join();
    }
}
