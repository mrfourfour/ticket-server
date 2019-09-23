package com.mrfofo.ticket;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getTicketTest() {
        Ticket ticket = Ticket.builder()
                .id("Ticket-2")
                .productId("Product-9")
                .status(Ticket.TicketStatus.NOT_USED.getValue())
                .date("2019-01-13")
                .qrData("sdfsdf")
                .amount(5)
                .totalPrice(27000)
                .build();

        webTestClient.get().uri("/api/ticket/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ticket.class)
                .isEqualTo(ticket);
    }

}
