package org.tbank.hw8.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final String secretKey = "7A5B713377684E693055426D673968734E2B573154424C646742734B6F755274";
    private final long jwtTenMinutesExpiration = 3600000;
    private final long jwtThirtyDaysExpiration = 3600000;

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtTenMinutesExpiration);
    }

    public String generateTokenWithRememberMeParameters(UserDetails userDetails, boolean rememberMe) {
        if (rememberMe) {
            return buildToken(new HashMap<>(), userDetails, jwtThirtyDaysExpiration);
        }

        return buildToken(new HashMap<>(), userDetails, jwtTenMinutesExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
