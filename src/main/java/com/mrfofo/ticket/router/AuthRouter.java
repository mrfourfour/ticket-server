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
public class AuthRouter {

    private final CognitoAuthHandler cognitoAuthHandler;

    @Bean
    public RouterFunction<?> authRoute() {
        return RouterFunctions
                .nest(path("/auth"), 
                        route(GET("/login"), cognitoAuthHandler::login)
                        .andRoute(GET("/token"), cognitoAuthHandler::token)
                        .andRoute(GET("/user/me"), cognitoAuthHandler::getCurrentUser)
                );
                
    }
}