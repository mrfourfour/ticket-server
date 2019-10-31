package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Ticket;
import com.mrfofo.ticket.model.Ticket.TicketStatus;
import com.mrfofo.ticket.repository.DynamoDbRepository;
import com.mrfofo.ticket.security.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketHandler {

    private final DynamoDbRepository<Ticket, String> repository;
    private final AuthService authService;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return repository.findAll().collectList().flatMap(tickets -> ServerResponse.ok().body(fromObject(Map.of("data", tickets))));
//        return ServerResponse.ok()
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(repository.findAll(), Ticket.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        try {
            String userId = authService.getClaims().getUuid();
            final String id = serverRequest.pathVariable("id");
            return repository.findById(id).flatMap(ticket -> StringUtils.equals(userId, id) 
                ? ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(Map.of("data", ticket)))
                : ServerResponse.status(HttpStatus.UNAUTHORIZED).body(BodyInserters.fromObject("남의 꺼 보는거 안대요 ㅎㅎ")));
        } catch(ParseException e) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).body(BodyInserters.fromObject("invalid Token"));
        }
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        try {
            String userId = authService.getClaims().getUuid();
            return serverRequest.bodyToMono(Ticket.class)
            .doOnNext(ticket -> {
                ticket.setId(UUID.randomUUID().toString());
                ticket.setUserId(userId);
                ticket.setDate(LocalDateTime.now().toString());
                ticket.setStatus(TicketStatus.NOT_USED);
            }).doOnNext(ticket -> {
                log.info(ticket.toString());
                repository.save(ticket);
            }).flatMap(ticket -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(ticket))
            );
        } catch(ParseException e) {
            e.printStackTrace();
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .body(BodyInserters.fromObject(e.getClass().getSimpleName()));
        }
    }
}
