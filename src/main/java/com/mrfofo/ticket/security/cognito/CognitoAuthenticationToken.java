package com.mrfofo.ticket.security.cognito;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Builder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Builder
public class CognitoAuthenticationToken extends AbstractAuthenticationToken {
    /**
     *
     */
    private static final long serialVersionUID = -5628088177137802689L;

    
    private String token;
    private JWTClaimsSet details;
    @Builder.Default
    private List<GrantedAuthority> authorities = List.of();

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public CognitoAuthenticationToken(String token, JWTClaimsSet details, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.details = details;

        setDetails(details);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return details;
    }
}
