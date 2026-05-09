package com.eightthreads.backend.security;

import com.eightthreads.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM = "role";
    private final Key signingKey;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret:change-this-secret-key-change-this-secret-key}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        String role = user.getRole() == null ? null : user.getRole().name();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get(ROLE_CLAIM, String.class);
        String authorityValue = role == null ? null : (role.startsWith("ROLE_") ? role : "ROLE_" + role);
        List<GrantedAuthority> authorities = authorityValue == null
                ? List.of()
                : List.of(new SimpleGrantedAuthority(authorityValue));
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
