package com.Peabody.deskhub.auth.service.Impl;


import com.Peabody.deskhub.auth.entity.User;
import com.Peabody.deskhub.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Override
    public String generateAccessToken(User user) {

        Map<String, Object> claims = Map.of(
                "employeeId", user.getEmployeeId()

        );

        return createAccessToken(claims, user);
    }

    @Override
    public String createAccessToken(
            Map<String, Object> claims,
            User user
    ) {

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmployeeId())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + accessTokenExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Method 2: Custom extraction logic for Employee ID from custom claims block
    public String extractEmployeeId(String token) {
        return extractClaim(token, claims -> claims.get("employeeId", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String tokenEmployeeId = extractEmployeeId(token);
        // Cast to your entity to access the getEmployeeId() getter method directly
        com.Peabody.deskhub.auth.entity.User user = (com.Peabody.deskhub.auth.entity.User) userDetails;
        return (tokenEmployeeId.equals(user.getEmployeeId()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}