package com.dev.skyskill.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResultVo {

  private String token;
  private LoginUserVo user;
}
