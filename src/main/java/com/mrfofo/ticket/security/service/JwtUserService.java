package com.mrfofo.ticket.security.service;

import com.mrfofo.ticket.model.User;
import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtUserService {
    User getUser(JWTClaimsSet tokenClaims);
}
