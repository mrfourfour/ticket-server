package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.repository.DynamoDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
@RequiredArgsConstructor
public class TicketHandler {

    private final DynamoDbRepository<Ticket, String> repository;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return repository.findAll().collectList().flatMap(tickets -> ServerResponse.ok().body(fromObject(Map.of("data", tickets))));
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(repository.findAll(), Ticket.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        final String id = serverRequest.pathVariable("id");
        return repository.findById(id).flatMap(ticket -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(Map.of("data", ticket))));
    }
}
