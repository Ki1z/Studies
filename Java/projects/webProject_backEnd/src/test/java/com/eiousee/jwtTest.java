package com.eiousee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class jwtTest {

    @Test
    public void generateJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "kiiz");
        claims.put("password", "123456");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "ZWlvdXNlZQ==")
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600))
                .compact();

        System.out.println(jwt);
    }

    @Test
    public void parseJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsInVzZXJuYW1lIjoia2lpeiIsImV4cCI6MTc3Njk0MTA0Nn0.4xM6yeaGiUuZPK0e4XIByi2MNPtMT8X3ZgIgR0PTW4E";
        Claims claims = Jwts.parser()
                .setSigningKey("ZWlvdXNlZQ==")
                .parseClaimsJws(jwt)
                .getBody();
        System.out.println(claims);
    }
}
