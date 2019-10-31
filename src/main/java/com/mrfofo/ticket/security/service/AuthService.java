package com.mrfofo.ticket.security.service;

import com.mrfofo.ticket.security.cognito.CognitoJWT;
import com.mrfofo.ticket.model.User;
import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.codec.http.HttpScheme;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

@Slf4j
@Component
public class AuthService {
    @Value("${cognito.domain}")
    private String baseUrl;
    @Value("${cognito.endpoints.token}")
    private String tokenUri;
    @Value("${cognito.client}")
    private String clientId;
    @Value("${cognito.secret}")
    private String clientSecret;
    @Value("${cognito.callback}")
    private String callbackUri;

    public Mono<CognitoJWT> getToken(String code) {
        String key = clientId + ":" + clientSecret;
        String auth = Base64Utils.encodeToString(key.getBytes(StandardCharsets.UTF_8));
        return WebClient.builder().baseUrl(baseUrl).build()
                .post()
                .uri(builder -> builder
                        .path(tokenUri)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .queryParam("redirect_uri", callbackUri)
                        .build())
                // .header("HeaderName", "value")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + auth)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(CognitoJWT.class);
                
        // return WebClient.create().get()
        //         .uri(builder -> {
        //             URI uri = builder
        //                 .scheme("https")
        //                 .host(host)
        //                 .path(tokenPath)
        //                 .queryParam("grant_type", "authorization_code")
        //                 .queryParam("client_id", clientId)
        //                 .queryParam("code", "code")
        //                 .queryParam("redirect_uri", callbackUri)
        //                 .build();
        //             log.info(uri.toString());
        //             return uri;
        //             })
        //         .header("HeaderName", "value")
        //         .header(HttpHeaders.AUTHORIZATION, "Basic " + auth)
        //         .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        //         .retrieve()
        //         .bodyToMono(CognitoJWT.class);
    }

    public User getClaims() throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTClaimsSet details = (JWTClaimsSet) authentication.getDetails();
        log.info(details.toJSONObject().toJSONString());
        return User.builder()
                .uuid(details.getStringClaim("sub"))
                .authTime((Long) (details.getClaim("auth_time")))
                .issued((Date) details.getClaim("iat"))
                .expire((Date) details.getClaim("exp"))
                // .name(details.getStringClaim("name"))
                .cognitoUserName(details.getStringClaim("username"))
                // .email(details.getStringClaim("email"))
                .build();
    }
}
