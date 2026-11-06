package com.dev.skyskill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.redis")
public class AppRedisProperties {

  private String keyPrefix = "";
}
