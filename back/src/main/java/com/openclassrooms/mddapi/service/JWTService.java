package com.openclassrooms.mddapi.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
    
    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;

    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Permit to generate a token by user email
     * @param email of the user
     * @return token
     */
    public String generateToken(String email) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.DAYS))
            .subject(email)
            .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    /**
     * Permit to decode the token
     * @param token to decode
     * @return user email
     */
    public String decodeToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    /**
     * Check if the token is always available
     * @param token to check
     * @return true if token is available and false if not
     */
    public Boolean isTokenValid(String token) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
            return decodedJwt.getExpiresAt().isAfter(Instant.now());
        } catch (JwtException e) {
            System.out.println("Error - isTokenValid" + e);
            return false;
        }
    }
}
