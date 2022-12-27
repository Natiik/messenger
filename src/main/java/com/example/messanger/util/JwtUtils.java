package com.example.messanger.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final String AUTHORITIES = "authorities";
    @Value("${messenger.token.ttl}")
    private long TTL;

    @Value("${messenger.token.refresh-ttl}")
    private long REFRESH_TTL ;

    @Value("${messenger.token.signing-key}")
    private String jwtSigningKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean hasClaim(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsGetter) {
        Claims claims = extractAllClaims(token);
        return claimsGetter.apply(claims);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(claims, userDetails, TTL);
    }

    public String generateRefreshToken(UserDetails userDetails, Map<String, Object> claims){
        return  createToken(claims, userDetails, REFRESH_TTL);
    }

    public boolean isTokenValid(String token, UserDetails details) {
        String username = extractUsername(token);
        return details.getUsername().equals(username) && !isTokenExpired(token);
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails, long ttl ) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim(AUTHORITIES, userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
    }
}
