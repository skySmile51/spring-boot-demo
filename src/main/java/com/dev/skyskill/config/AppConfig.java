package com.dev.skyskill.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AppJwtProperties.class, AppRedisProperties.class})
public class AppConfig {}
