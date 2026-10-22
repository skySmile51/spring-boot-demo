package com.dev.skyskill.mapper;

import com.dev.skyskill.dao.entity.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  void selectById_whenExists_returnsUser() {
    UserDO user = userMapper.selectById(1L);
    assertNotNull(user);
    assertNotNull(user.getId());
  }
}
