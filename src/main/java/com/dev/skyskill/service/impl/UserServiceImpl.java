package com.dev.skyskill.service.impl;

import com.dev.skyskill.mapper.UserMapper;
import com.dev.skyskill.dao.entity.UserDO;
import com.dev.skyskill.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  public UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public UserDO getUserById(Long id) {
    UserDO user = userMapper.selectById(id);
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    return user;
  }
}
