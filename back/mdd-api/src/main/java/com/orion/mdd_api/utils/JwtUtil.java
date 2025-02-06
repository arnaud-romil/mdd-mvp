package com.orion.mdd_api.utils;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.accesstoken.validity}")
    private long tokenValidity;

    @Value("${spring.application.name}")
    private String appName;

    public JwtUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateAccessToken(String userEmail) {
        
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer(appName)
        .issuedAt(now)
        .expiresAt(now.plusSeconds(tokenValidity))
        .subject(userEmail)
        .claim("scope", "")
        .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }



}
