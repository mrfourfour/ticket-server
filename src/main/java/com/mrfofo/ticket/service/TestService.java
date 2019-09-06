package com.mrfofo.ticket.service;

import com.mrfofo.ticket.model.Test;
import com.mrfofo.ticket.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository repository;

    public Mono<Test> find(Long id) {
        return repository.findById(id);
    }

}
