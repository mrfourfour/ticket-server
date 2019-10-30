package com.mrfofo.ticket.security.cognito;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URI;

@Configuration
public class JwtProcessor {
    @Value("${cognito.jwk}")
    private String jwkKeys;
    @Value("${cognito.connectTimeout}")
    private Integer connectTimeout;
    @Value("${cognito.readTimeout}")
    private Integer readTimeout;

    @Bean
    public ConfigurableJWTProcessor<?> configurableJWTProcessor() throws MalformedURLException {
        ResourceRetriever resourceRetriever = new DefaultResourceRetriever(connectTimeout, readTimeout);
        URI jwkUri = URI.create(jwkKeys);
        JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkUri.toURL(), resourceRetriever);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }
}
