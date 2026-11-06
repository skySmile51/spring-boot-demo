package com.dev.skyskill.dto.login;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserVo {

  private String userId;
  private String username;
  private String phone;
  private String avatar;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
