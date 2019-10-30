package com.mrfofo.ticket.model;

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
public class User {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("auth_time")
    private Long authTime;
    private Date issued;
    private Date expire;
    private String cognitoUserName;
}