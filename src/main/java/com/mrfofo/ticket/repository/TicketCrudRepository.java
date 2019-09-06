package com.mrfofo.ticket.repository;

import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface TicketCrudRepository<T, I> {
    Mono<T> findById(I i);
    Mono<T> save(T t);
    Mono<Boolean> deleteById(I i);
    Flux<T> findAll();
}
