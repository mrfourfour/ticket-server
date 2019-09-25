package com.mrfofo.ticket.router;

import com.mrfofo.ticket.handler.ErrorHandler;
import com.mrfofo.ticket.handler.HealthHandler;
import com.mrfofo.ticket.handler.TicketHandler;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TicketRouter {

    private final TicketHandler ticketHandler;
    private final HealthHandler healthHandler;
    private final ErrorHandler errorHandler;

    @Bean
    public RouterFunction<?> ticketRoute() {
        return RouterFunctions
                .nest(path("/"),
                        route(GET("/"), healthHandler::checkHealth))

                .andNest(path("/api/ticket"),
                        route(GET("/"), ticketHandler::findAll)
                        .andRoute(GET("/{id}"), ticketHandler::findById))

                .andOther(route(all(), errorHandler::notFound));
    }
}
