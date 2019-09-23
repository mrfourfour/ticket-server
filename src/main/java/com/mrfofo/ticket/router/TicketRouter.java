package com.mrfofo.ticket.router;

import com.mrfofo.ticket.handler.TicketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.http.HttpMethod.*;
@Configuration
public class TicketRouter {
    @Bean
    public RouterFunction<?> doRoute(TicketHandler ticketHandler) {
        return RouterFunctions.
                nest(path("/api/ticket"),
                        route(GET("/"), ticketHandler::findAll)
                        .andRoute(GET("/{id}"), ticketHandler::find)
                );
    }
}
