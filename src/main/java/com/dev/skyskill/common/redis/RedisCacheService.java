package com.dev.skyskill.common.redis;

import com.dev.skyskill.config.AppRedisProperties;
import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService {

  private final StringRedisTemplate redisTemplate;
  private final String keyPrefix;

  public RedisCacheService(StringRedisTemplate redisTemplate, AppRedisProperties redisProperties) {
    this.redisTemplate = redisTemplate;
    this.keyPrefix = redisProperties.getKeyPrefix() != null ? redisProperties.getKeyPrefix() : "";
  }

  public boolean exists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(prefixed(key)));
  }

  public void set(String key, String value, Duration ttl) {
    if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
      redisTemplate.opsForValue().set(prefixed(key), value, ttl);
    } else {
      redisTemplate.opsForValue().set(prefixed(key), value);
    }
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(prefixed(key));
  }

  public void delete(String key) {
    redisTemplate.delete(prefixed(key));
  }

  public Long increment(String key) {
    return redisTemplate.opsForValue().increment(prefixed(key));
  }

  private String prefixed(String key) {
    return keyPrefix + key;
  }
}
