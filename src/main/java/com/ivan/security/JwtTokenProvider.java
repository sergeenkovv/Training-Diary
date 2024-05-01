package com.ivan.security;

import com.ivan.model.Athlete;
import com.ivan.service.AthleteService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {

    private final Long access;
    private final Key key;
    private final AthleteService athleteService;

    public JwtTokenProvider(String secret, Long access, AthleteService athleteService) {
        this.access = access;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.athleteService = athleteService;
    }

    public String createAccessToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date validity = new Date(now.getTime() + access);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();

        return accessToken;
    }

    public Authentication authentication(String token) throws AccessDeniedException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied: Invalid token" + token);
        }

        String login = getLoginFromToken(token);
        Athlete athlete = athleteService.getByLogin(login);

        return new Authentication(login, athlete.getRole(), true, "Successful login");
    }

    public String getLoginFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) throws RuntimeException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }
}