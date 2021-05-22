package com.erclub.sms.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtils {

  @Value("${token.secret:erclub}")
  private String secret;

  public static final long JWT_TOKEN_VALIDITY = 8 * 60 * 60;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getId);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public String generateToken(String userId) {
    return generateToken(userId, new HashMap<>());
  }

  public String generateToken(String userId, Map<String, Object> claims) {
    return doGenerateToken(userId, claims);
  }

  private String doGenerateToken(String userId, Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setId(userId)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .setSubject(userId)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(String token, String requestHeaderUserId) {
    final String username = getUsernameFromToken(token);
    return (username.equals(requestHeaderUserId) && !isTokenExpired(token));
  }

}

