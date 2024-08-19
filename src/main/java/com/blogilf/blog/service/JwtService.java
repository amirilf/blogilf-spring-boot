package com.blogilf.blog.service;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Base64;
import java.util.function.Function;

@Component
public class JwtService {

    private String secretKey;
    private final long expirationTime = 1000 * 60 * 15; // 15 min

    public JwtService(){
        generateSecretKey();
    }

    private void generateSecretKey() {
        try {
            KeyGenerator keyGan = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGan.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
        
    public String generateToken(String username,String role){

        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);

        Key key = getKey();

        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .and()
            .signWith(key)
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token,Function<Claims,T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Map<String, String> extractUsernameAndRole(String token){
        
        Claims claims = extractAllClaims(token);
        
        String username = claims.getSubject();
        String role = (String) claims.get("role");

        Map<String, String> result = new HashMap<>();
        
        result.put("username", username);
        result.put("role", role);
        
        return result;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
    
    public boolean validateToken(String token) {
        return extractClaim(token, Claims::getExpiration).after(new Date());
    }

}
