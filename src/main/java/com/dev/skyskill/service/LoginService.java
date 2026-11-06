package com.dev.skyskill.service;

import com.dev.skyskill.common.auth.JwtPayload;
import com.dev.skyskill.dto.login.LoginResultVo;
import com.dev.skyskill.dto.login.SmsLoginQuery;
import com.dev.skyskill.dto.login.SmsLoginRequest;

public interface LoginService {

  String sendSms(SmsLoginRequest request);

  LoginResultVo loginByPhone(SmsLoginQuery query);

  Void logout(JwtPayload user);
}
