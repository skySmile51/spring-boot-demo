package com.dev.skyskill.service;


import com.dev.skyskill.dao.entity.UserDO;

public interface UserService {
  UserDO getUserById(Long id);

  /** 按手机号查询用户，不存在时返回 null */
  UserDO findByPhone(String phone);
}