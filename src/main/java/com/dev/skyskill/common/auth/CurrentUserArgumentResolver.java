package com.dev.skyskill.common.auth;

import com.dev.skyskill.common.exception.UnauthorizedException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class)
        && JwtPayload.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Object user =
        webRequest.getAttribute(
            JwtAuthInterceptor.REQUEST_ATTR_JWT_USER, NativeWebRequest.SCOPE_REQUEST);
    if (!(user instanceof JwtPayload jwtPayload)) {
      throw new UnauthorizedException("登录状态已失效，请重新登录");
    }
    return jwtPayload;
  }
}
