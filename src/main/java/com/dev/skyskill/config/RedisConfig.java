package com.dev.skyskill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 显式声明 Boot 也会自动注册的 Bean，便于 IDE 识别注入点。
 * Boot 侧对 {@link StringRedisTemplate} 带 {@code @ConditionalOnMissingBean}，此处先注册后不会重复。
 */
@Configuration
public class RedisConfig {

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(connectionFactory);
    return template;
  }
}
