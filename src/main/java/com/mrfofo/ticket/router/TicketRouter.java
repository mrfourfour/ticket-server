package com.mrfofo.ticket.router;

import com.mrfofo.ticket.handler.*;
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
    private final CategoryHandler categoryHandler;
    private final AreaHandler areaHandler;

    @Bean
    public RouterFunction<?> ticketRoute() {
        return RouterFunctions
                .nest(path("/"),
                        route(GET("/"), healthHandler::checkHealth))

                .andNest(path("/api/ticket"),
                        route(GET("/"), ticketHandler::findAll)
                        .andRoute(POST("/"), ticketHandler::save)
                        .andRoute(GET("/{id}"), ticketHandler::findById))

                .andNest(path("/api/product"),
                        route(GET("/"), productHandler::findAll)
                        .andRoute(GET("/area/{area}/category/{category}"), productHandler::findByAreaAndCategory)
                        .andRoute(GET("/{id}"), productHandler::findById)
                        .andRoute(POST("/{id}"), productHandler::saveReview)
                        // .andRoute(POST("/{id}/option"), productHandler::addOption)
                )

                .andNest(path("/api/category"),
                        route(GET("/"), categoryHandler::findCategory))

                .andNest(path("/api/area"),
                        route(GET("/"), areaHandler::findArea))


                .andOther(route(all(), errorHandler::notFound));
    }
}
