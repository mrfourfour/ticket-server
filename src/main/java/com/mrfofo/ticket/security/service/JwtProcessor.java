package com.mrfofo.ticket.security.service;

import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtProcessor {
    JWTClaimsSet processJwt(String token);
}
