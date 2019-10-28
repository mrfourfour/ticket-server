package com.mrfofo.ticket;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Ticket;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketWebTests {

    @Autowired
    public WebTestClient webTestClient;

    @Test
//    @Ignore
    public void createTicket() {
        Ticket ticket = Ticket.builder()
            .productId("Product-1")
            .userId("왕밤빵")
            .totalPrice(30000L)
            .amount(3)
//            .qrData("guegue")
            .build();

        webTestClient.post().uri("/api/ticket/")
        .body(Mono.just(ticket), Ticket.class)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk();
    }

    @Test
    @Ignore
    public void addReview() {

    }

}