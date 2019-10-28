package com.mrfofo.ticket.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.server.ServerRequest;

public interface AuthenticationService {
    Authentication authenticate(ServerRequest request);
}
