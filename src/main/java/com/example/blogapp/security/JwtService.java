package com.example.blogapp.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.blogapp.domain.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final SecretKey secretKey;
  private final long expirationMs;

  public JwtService(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-ms}") long expirationMs) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId().toString());
    claims.put("name", user.getName());

    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getEmail())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(secretKey)
        .compact();
  }

  public boolean isTokenValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String extractEmail(String token) {
    return parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
  }
}
