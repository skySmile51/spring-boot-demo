package com.dev.skyskill.service;


import com.dev.skyskill.dao.entity.UserDO;

public interface UserService {
  UserDO getUserById(Long id);
}