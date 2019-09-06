package com.mrfofo.ticket;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamoDBTests {

    @Autowired
    DynamoDbAsyncClient dynamoDbAsyncClient;

    /**
     * table 작성 예시 . DynamoDB
     */
    @Test
    public void createTableTest() {
        CreateTableRequest createTestTableRequest = CreateTableRequest.builder()
                .tableName("test")
                .attributeDefinitions(
                        AttributeDefinition.builder().attributeName("id").attributeType(ScalarAttributeType.N).build(),
                        AttributeDefinition.builder().attributeName("title").attributeType(ScalarAttributeType.S).build()
                ).keySchema(
                        KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build()
                ).globalSecondaryIndexes(GlobalSecondaryIndex.builder()
                        .indexName("byTitle")
                        .keySchema(
                                KeySchemaElement.builder().attributeName("title").keyType(KeyType.HASH).build()
                        ).build()
                ).provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(1L).writeCapacityUnits(1L).build()).build();
        CompletableFuture<CreateTableResponse> res = dynamoDbAsyncClient.createTable(createTestTableRequest);
        assertThat(res.isDone(), is(true));
    }
}
