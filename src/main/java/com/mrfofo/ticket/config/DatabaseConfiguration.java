package com.mrfofo.ticket.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
public class DatabaseConfiguration extends AbstractR2dbcConfiguration {
    @Override
    public ConnectionFactory connectionFactory() {
        return null;
    }
}
