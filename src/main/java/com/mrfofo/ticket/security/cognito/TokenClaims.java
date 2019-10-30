package com.mrfofo.ticket.security.cognito;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenClaims {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("auth_time")
    private Long authTime;
    private Date issued;
    private Date expire;
    private String name;
    private String cognitoUserName;
    private String email;
}