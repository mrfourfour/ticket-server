package com.mrfofo.ticket.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
@Getter
public class DynamoProperties {
    private final String endPoint = "http://127.0.0.1:8000";
    private final String region = Region.AP_NORTHEAST_2.id();
}
