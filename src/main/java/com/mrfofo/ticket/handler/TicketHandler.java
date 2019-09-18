package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Ticket;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class TicketHandler {
    public Mono<ServerResponse> hello(ServerRequest serverRequest) {
        Mono<Ticket> ticketMono = Mono.just(Ticket.builder()
                .id("Ticket-1")
                .product(Product.builder().id("Product-1").name("요트한직?").build())
                .status("미사용")
                .build()
        );

        return ticketMono.flatMap(ticket -> ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(ticket)));
    }
}
