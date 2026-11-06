package com.dev.skyskill.service.impl;

import com.dev.skyskill.common.auth.JwtPayload;
import com.dev.skyskill.common.auth.JwtService;
import com.dev.skyskill.common.redis.RedisCacheService;
import com.dev.skyskill.dao.entity.UserDO;
import com.dev.skyskill.dto.login.LoginResultVo;
import com.dev.skyskill.dto.login.LoginUserVo;
import com.dev.skyskill.dto.login.SmsLoginQuery;
import com.dev.skyskill.dto.login.SmsLoginRequest;
import com.dev.skyskill.service.LoginService;
import com.dev.skyskill.service.UserService;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
  // 验证码有效期时间：5分钟
  private static final Duration SMS_CODE_TTL = Duration.ofMinutes(5);
  // 有效期时间：30天
  private static final Duration SESSION_TTL = Duration.ofDays(30);

  private final UserService userService;
  private final RedisCacheService redisCacheService;
  private final JwtService jwtService;

  public LoginServiceImpl(
      UserService userService, RedisCacheService redisCacheService, JwtService jwtService) {
    this.userService = userService;
    this.redisCacheService = redisCacheService;
    this.jwtService = jwtService;
  }

  @Override
  public String sendSms(SmsLoginRequest request) {
    if (redisCacheService.exists("sms_number")) {
      redisCacheService.increment("sms_number");
    } else {
      redisCacheService.set("sms_number", "1", null);
    }

    int code = ThreadLocalRandom.current().nextInt(1000, 10000);
    redisCacheService.set(request.getPhone(), String.valueOf(code), SMS_CODE_TTL);
    return String.valueOf(code);
  }

  @Override
  public LoginResultVo loginByPhone(SmsLoginQuery query) {
    UserDO user = userService.findByPhone(query.getPhone());
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    if (!query.getPassword().equals(user.getPassword())) {
      throw new RuntimeException("密码错误！");
    }

    JwtPayload jwtPayload =
        new JwtPayload(user.getUserId(), user.getUsername(), user.getPhone());
    String token = jwtService.sign(jwtPayload);
    redisCacheService.set("mall_user:" + user.getUserId(), token, SESSION_TTL);

    LoginUserVo loginUser =
        LoginUserVo.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .phone(user.getPhone())
            .avatar(user.getAvatar())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();

    return new LoginResultVo(token, loginUser);
  }

  @Override
  public Void logout(JwtPayload user) {
    redisCacheService.delete("mall_user:" + user.getUserId());
    return null;
  }
}
