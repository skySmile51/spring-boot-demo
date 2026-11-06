package com.dev.skyskill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {

  private String secret;
  private long expiresInSeconds = 36000;
}
