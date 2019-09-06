package com.mrfofo.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

import java.net.URI;

@Configuration
public class DynamoConfig {
    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(DynamoProperties dynamoProperties) {
        DynamoDbAsyncClientBuilder dynamoDbAsyncClientBuilder = DynamoDbAsyncClient.builder()
                .region(Region.of(dynamoProperties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create());

        if(!StringUtils.isEmpty(dynamoProperties.getEndPoint())) {
            dynamoDbAsyncClientBuilder.endpointOverride(URI.create(dynamoProperties.getEndPoint()));
        }

        return dynamoDbAsyncClientBuilder.build();
    }
}