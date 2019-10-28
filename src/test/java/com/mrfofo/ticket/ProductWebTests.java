package com.mrfofo.ticket;

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

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mrfofo.ticket.model.Product;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductWebTests {

    @Autowired
    public WebTestClient webTestClient;

    @Test
    @Ignore
    public void createProductTest() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("웹테스트")
                .sellerId("Seller-1")
                .image("https://avatars0.githubusercontent.com/u/54847050?s=200&v=4")
                .category(Product.ProductCategory.LEISURE)
                .subCategory(Product.ProductSubCategory.LAND)
                .info("잘되나요?")
                .area(Product.ProductArea.JEJU)
                .price(30000L)
                .options(List.of(
                        Product.ProductOption.builder()
                                .id(UUID.randomUUID().toString())
                                .amount(99L)
                                .date(LocalDateTime.of(2019, 11, 1, 9, 0, 0).toString())
                                .description("설명테스트에요")
                                .build()
                ))
                .date(LocalDateTime.now().toString())
                .build();

        webTestClient.post().uri("/api/product")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .body(Mono.just(product), Product.class)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    @Ignore
    public void createTicketTest() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("웹테스트")
                .sellerId("Seller-1")
                .image("https://avatars0.githubusercontent.com/u/54847050?s=200&v=4")
                .category(Product.ProductCategory.LEISURE)
                .subCategory(Product.ProductSubCategory.LAND)
                .info("잘되나요?")
                .area(Product.ProductArea.JEJU)
                .price(30000L)
                .options(List.of(
                        Product.ProductOption.builder()
                                .id(UUID.randomUUID().toString())
                                .amount(99L)
                                .date(LocalDateTime.of(2019, 11, 1, 9, 0, 0).toString())
                                .description("설명테스트에요")
                                .build()
                ))
                .date(LocalDateTime.now().toString())
                .build();

        // 클라이언트에서 떤져줘야되는

        int amount = 3;
        long totalPrice = product.getPrice() * 3;
        String selectedProductId = product.getId();
        String selectedOptionId = product.getOptions().get(0).getId();
        String userId = "fromCognitoId";

        Ticket ticket = Ticket.builder()
                .amount(3)
                .totalPrice(totalPrice)
                .productId(selectedProductId)
                .optionId(selectedOptionId)
                .userId(userId)
                .build();



    }

}