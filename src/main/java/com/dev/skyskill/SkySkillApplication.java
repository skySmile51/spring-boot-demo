package com.dev.skyskill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dev.skyskill.dao.mapper")
public class SkySkillApplication {

  public static void main(String[] args) {
    SpringApplication.run(SkySkillApplication.class, args);
  }
}
