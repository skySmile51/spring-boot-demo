package com.dev.skyskill.controller;

import com.dev.skyskill.dao.entity.UserDO;
import com.dev.skyskill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users") // 统一给这个 Controller 的接口加上前缀
public class UserController {

  // 注入 Service 层。企业里推荐使用构造器注入（这里为了直观用 @Autowired 字段注入，Spring 官方更推荐构造器注入）
  @Autowired
  private UserService userService;

  // 访问路径：GET /api/users/1
  @GetMapping("/{id}")
  public UserDO getUser(@PathVariable Long id) {
    // Controller 完全不关心数据怎么来的，直接调用 Service
    return userService.getUserById(id);
  }
}