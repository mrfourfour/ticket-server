package com.mrfofo.ticket.handler;

import com.mrfofo.ticket.model.Ticket;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class TicketHandler {
    public Flux<Ticket> ticketList() {
        return Flux.just(
                Ticket.builder().id("Ticket-1").productId("Product-1").status(Ticket.TicketStatus.NOT_USED.getValue()).amount(1).totalPrice(30000L).date("2018-11-27").qrData("soohyeon").build(),
                Ticket.builder().id("Ticket-2").productId("Product-9").status(Ticket.TicketStatus.NOT_USED.getValue()).amount(5).totalPrice(27000L).date("2019-01-13").qrData("sdfsdf").build(),
                Ticket.builder().id("Ticket-3").productId("Product-3").status(Ticket.TicketStatus.NOT_USED.getValue()).amount(2).totalPrice(120000L).date("2018-12-25").qrData("baem").build()
        );
    }

    public Mono<ServerResponse> hello(ServerRequest serverRequest) {
        Mono<Ticket> ticketMono = Mono.just(Ticket.builder()
                .id("Ticket-1")
                .productId("Product-1")
                .status("미사용")
                .build()
        );

        return ticketMono.flatMap(ticket -> ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(ticket)));
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ticketList().collectList()
                .flatMap(tickets ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .body(fromObject(tickets)));
    }

    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        final String id = serverRequest.pathVariable("id");
        return ticketList().collectList().flatMap(tickets -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(
                fromObject(tickets.get(Integer.parseInt(id)))
        ));
    }
}
