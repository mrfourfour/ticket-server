package com.mrfofo.ticket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import java.net.URI;

@Configuration
public class DynamoConfig {
    @Value("${aws.access.key}")
    private String AWS_ACCESS_KEY;
    @Value("${aws.secret.key}")
    private String AWS_SECRET_KEY;
    @Value("${dynamodb.endpoint}")
    private String dynamoDbEndpoint;
    @Bean
    public DynamoDbAsyncClient dynamoDBAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        AWS_ACCESS_KEY, AWS_SECRET_KEY
                )))
                .endpointOverride(URI.create(dynamoDbEndpoint))
                .build();
    }
}
