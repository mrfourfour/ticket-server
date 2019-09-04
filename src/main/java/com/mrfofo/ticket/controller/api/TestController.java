package com.mrfofo.ticket.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class TestController {
    @GetMapping
    public Mono<String> index() {
        return Mono.just("hi");
    }
}
