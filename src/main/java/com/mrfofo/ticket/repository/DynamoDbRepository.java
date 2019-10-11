package com.mrfofo.ticket.repository;

import com.mrfofo.ticket.exception.NotEqualException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DynamoDbRepository<T, I> {
    Flux<T> findAll();
    Mono<T> findById(final I id);
    Mono<T> save(final T t) throws NotEqualException;
    Mono<Void> delete();
}
