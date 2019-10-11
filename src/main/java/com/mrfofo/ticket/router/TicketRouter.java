package com.mrfofo.ticket.router;

import com.mrfofo.ticket.handler.ErrorHandler;
import com.mrfofo.ticket.handler.HealthHandler;
import com.mrfofo.ticket.handler.ProductHandler;
import com.mrfofo.ticket.handler.TicketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class TicketRouter {

    private final TicketHandler ticketHandler;
    private final HealthHandler healthHandler;
    private final ErrorHandler errorHandler;
    private final ProductHandler productHandler;

    @Bean
    public RouterFunction<?> ticketRoute() {
        return RouterFunctions
                .nest(path("/"),
                        route(GET("/"), healthHandler::checkHealth))
                .andNest(path("/api/ticket"),
                        route(GET("/"), ticketHandler::findAll)
                        .andRoute(GET("/{id}"), ticketHandler::findById))
                .andNest(path("/api/product"),
                        route(GET("/"), productHandler::findAll)
                        .andRoute(POST("/"), productHandler::save)
                        .andRoute(GET("/category/{category}"), productHandler::findByCategory)
                        .andRoute(GET("/{id}"), productHandler::findById))
                .andOther(route(all(), errorHandler::notFound));
    }
}
