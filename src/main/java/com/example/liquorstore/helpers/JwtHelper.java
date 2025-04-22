package com.example.liquorstore.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtHelper {
    static byte[] keyBytes = Decoders.BASE64.decode("7xvG7s/fdDmDkY5sBNNLietiaB6HkXu/uYgviYCnBdU=");
    static Key key = Keys.hmacShaKeyFor(keyBytes);
    private static final int MINUTES = 60;
    private static final int REFRESH_TOKEN_DAYS = 7;

    public static String generateToken(String identifier) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(identifier)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    public static String generateRefreshToken(String identifier) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(identifier)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS)))
                .signWith(key)
                .compact();
    }

    public static String extractUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private static Claims getTokenBody(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            throw new BadCredentialsException("Invalid token signature");
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Token has expired");
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token");
        }
    }

    private static boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }
}
