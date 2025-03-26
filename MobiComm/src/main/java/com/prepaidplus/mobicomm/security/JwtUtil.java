package com.prepaidplus.mobicomm.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final Key key;
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 30; // 30 minutes
    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7; // 7 days

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /** ✅ Generates JWT Token (Access Token) */
    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)  // This can be username (for admin) or phoneNumber (for customers)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /** ✅ Generates Refresh Token */
    public String generateRefreshToken(String phoneNumber) {
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** ✅ Extracts Phone Number from Token */
    public String getPhoneNumberFromToken(String token) { 
        return getClaimsFromToken(token).getSubject(); // Can be phone or username
    }


    /** ✅ Extracts Role from Token */
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    /** ✅ Validates Token */
    public boolean validateToken(String token) {
        try {
            System.out.println("🔍 Validating Token: " + token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("🚨 Token Expired at: " + e.getClaims().getExpiration());
        } catch (JwtException e) {
            System.out.println("🚨 Invalid Token: " + e.getMessage());
        }
        return false;
    }

    /** ✅ Checks if Token is Expired */
    public boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    /** ✅ Retrieves Authentication from Token */
    public UsernamePasswordAuthenticationToken getAuthentication(String token, UserDetails userDetails) {
        String role = getRoleFromToken(token);
        List<GrantedAuthority> authorities = role != null ? List.of(new SimpleGrantedAuthority(role)) : Collections.emptyList();
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    /** ✅ Parses JWT Claims */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
