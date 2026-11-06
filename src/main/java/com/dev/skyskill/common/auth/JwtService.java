package com.dev.skyskill.common.auth;

import com.dev.skyskill.config.AppJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey secretKey;
  private final long expiresInSeconds;

  public JwtService(AppJwtProperties jwtProperties) {
    this.secretKey =
        Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    this.expiresInSeconds = jwtProperties.getExpiresInSeconds();
  }

  public String sign(JwtPayload payload) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", payload.getUserId());
    claims.put("username", payload.getUsername());
    claims.put("phone", payload.getPhone());

    Instant now = Instant.now();
    return Jwts.builder()
        .claims(claims)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(expiresInSeconds)))
        .signWith(secretKey)
        .compact();
  }

  public JwtPayload parse(String token) {
    try {
      Claims claims =
          Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(token)
              .getPayload();
      return new JwtPayload(
          stringClaim(claims, "userId"),
          stringClaim(claims, "username"),
          stringClaim(claims, "phone"));
    } catch (ExpiredJwtException ex) {
      throw new com.dev.skyskill.common.exception.UnauthorizedException("登录已过期，请重新登录");
    } catch (Exception ex) {
      throw new com.dev.skyskill.common.exception.UnauthorizedException(
          "未登录或访问令牌无效，请重新登录");
    }
  }

  private static String stringClaim(Claims claims, String name) {
    Object value = claims.get(name);
    return value != null ? value.toString() : null;
  }
}
