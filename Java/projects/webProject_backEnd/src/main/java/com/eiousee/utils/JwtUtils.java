package com.eiousee.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String KEY = "ZWlvdXNlZQ==";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    /**
     * 生成jwt
     * @param claims
     * @return 用户token
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .compact();
    }

    /**
     * 解析jwt
     * @param token
     * @return 用户信息
     */
    public static Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
