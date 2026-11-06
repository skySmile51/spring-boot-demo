package com.dev.skyskill.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsLoginRequest {

  @NotBlank(message = "phone 不能为空")
  private String phone;
}
