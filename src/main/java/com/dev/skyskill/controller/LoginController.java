package com.dev.skyskill.controller;

import com.dev.skyskill.common.auth.CurrentUser;
import com.dev.skyskill.common.auth.JwtPayload;
import com.dev.skyskill.common.auth.RequireJwt;
import com.dev.skyskill.common.web.ApiResponse;
import com.dev.skyskill.dto.login.LoginResultVo;
import com.dev.skyskill.dto.login.SmsLoginQuery;
import com.dev.skyskill.dto.login.SmsLoginRequest;
import com.dev.skyskill.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/login")
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/send")
  public ApiResponse<String> sendSms(@Valid @ModelAttribute SmsLoginRequest request) {
    return ApiResponse.ok(loginService.sendSms(request));
  }

  @GetMapping("/loginByPhone")
  public ApiResponse<LoginResultVo> loginByPhone(@Validated SmsLoginQuery query) {
    return ApiResponse.ok(loginService.loginByPhone(query));
  }

  @RequireJwt
  @GetMapping("/logout")
  public ApiResponse<Void> logout(@CurrentUser JwtPayload user) {
    return ApiResponse.ok(loginService.logout(user));
  }
}
