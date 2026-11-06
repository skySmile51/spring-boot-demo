package com.dev.skyskill.config;

import com.dev.skyskill.common.auth.CurrentUserArgumentResolver;
import com.dev.skyskill.common.auth.JwtAuthInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final JwtAuthInterceptor jwtAuthInterceptor;
  private final CurrentUserArgumentResolver currentUserArgumentResolver;

  public WebMvcConfig(
      JwtAuthInterceptor jwtAuthInterceptor,
      CurrentUserArgumentResolver currentUserArgumentResolver) {
    this.jwtAuthInterceptor = jwtAuthInterceptor;
    this.currentUserArgumentResolver = currentUserArgumentResolver;
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    // 为所有带有 @RestController 注解的类添加 /api 前缀
    configurer.addPathPrefix("/api",
            c -> c.isAnnotationPresent(RestController.class));
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtAuthInterceptor);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserArgumentResolver);
  }
}
