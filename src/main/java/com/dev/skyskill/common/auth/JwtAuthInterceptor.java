package com.dev.skyskill.common.auth;

import com.dev.skyskill.common.exception.UnauthorizedException;
import com.dev.skyskill.common.redis.RedisCacheService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

  public static final String REQUEST_ATTR_JWT_USER = "jwtUser";

  private final JwtService jwtService;
  private final RedisCacheService redisCacheService;

  public JwtAuthInterceptor(JwtService jwtService, RedisCacheService redisCacheService) {
    this.jwtService = jwtService;
    this.redisCacheService = redisCacheService;
  }

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }
    if (!requiresJwt(handlerMethod)) {
      return true;
    }

    String token = extractBearerToken(request);
    if (token == null) {
      throw new UnauthorizedException("缺少访问令牌，请重新登录");
    }

    JwtPayload payload;
    try {
      payload = jwtService.parse(token);
    } catch (UnauthorizedException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new UnauthorizedException("访问令牌无效或已过期，请重新登录");
    }

    String redisKey = "mall_user:" + payload.getUserId();
    String currentToken = redisCacheService.get(redisKey);
    if (currentToken == null) {
      throw new UnauthorizedException("登录状态已失效，请重新登录");
    }
    if (!currentToken.equals(token)) {
      throw new UnauthorizedException("账号已在其它设备登录，请重新登录");
    }

    request.setAttribute(REQUEST_ATTR_JWT_USER, payload);
    return true;
  }

  /**
   * 方法级注解优先于类级：{@link RequestPublic} 可覆盖类上的 {@link RequireJwt}，反之亦然。
   */
  static boolean requiresJwt(HandlerMethod handlerMethod) {
    var method = handlerMethod.getMethod();
    var beanType = handlerMethod.getBeanType();

    if (AnnotatedElementUtils.hasAnnotation(method, RequestPublic.class)) {
      return false;
    }
    if (AnnotatedElementUtils.hasAnnotation(method, RequireJwt.class)) {
      return true;
    }
    if (AnnotatedElementUtils.hasAnnotation(beanType, RequestPublic.class)) {
      return false;
    }
    if (AnnotatedElementUtils.hasAnnotation(beanType, RequireJwt.class)) {
      return true;
    }
    return false;
  }

  private static String extractBearerToken(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (authorization == null || authorization.isBlank()) {
      return null;
    }
    String trimmed = authorization.trim();
    if (trimmed.length() > 7 && trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
      return trimmed.substring(7).trim();
    }
    return trimmed;
  }
}
