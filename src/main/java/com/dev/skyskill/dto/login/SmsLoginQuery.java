package com.dev.skyskill.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsLoginQuery {

  @NotBlank(message = "phone 不能为空")
  private String phone;

  @NotBlank(message = "password 不能为空")
  private String password;
}
