package com.mrfofo.ticket.security.cognito;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CognitoJWT {
    @JsonProperty("id_token")
    private String idToken;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private String expiresIn;
    @JsonProperty("token_type")
    private String tokenType;
}
