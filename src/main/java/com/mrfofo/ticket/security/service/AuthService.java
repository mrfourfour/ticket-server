package com.mrfofo.ticket.security.service;

import com.mrfofo.ticket.security.cognito.CognitoJWT;
import com.mrfofo.ticket.security.cognito.TokenClaims;
import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

@Slf4j
@Component
public class AuthService {
    @Value("${cognito.endpoints.token}")
    private String tokenUri;
    @Value("${cognito.client}")
    private String clientId;
    @Value("${cognito.secret}")
    private String clientSecret;
    @Value("${cognito.callback}")
    private String callbackUri;

    public CognitoJWT getToken(String code) {
        RestTemplate client = new RestTemplate();
        String key = clientId + ":" + clientSecret;
        String auth = Base64Utils.encodeToString(key.getBytes(StandardCharsets.UTF_8));

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>() {
            {
                add("HeaderName", "value");
                add("Authorization", "Basic " + auth);
                add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            }
        };

        HttpEntity<Void> req = new HttpEntity<>(null, headers);
        String url = tokenUri + "?grant_type=authorization_code&client_id=" + clientId + "&code=" + code
                + "&redirect_uri=" + callbackUri;

        return client.postForObject(url, req, CognitoJWT.class);
    }

    public TokenClaims getClaims() throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTClaimsSet details = (JWTClaimsSet) authentication.getDetails();
        log.info(details.toJSONObject().toJSONString());
        return TokenClaims.builder()
                .uuid(details.getStringClaim("sub"))
                .authTime((Long) (details.getClaim("auth_time")))
                .issued((Date) details.getClaim("iat"))
                .expire((Date) details.getClaim("exp"))
                .name(details.getStringClaim("name"))
                .cognitoUserName(details.getStringClaim("username"))
                .email(details.getStringClaim("email"))
                .build();
    }
}
