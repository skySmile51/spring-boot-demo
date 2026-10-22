package com.dev.skyskill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dev.skyskill.dao.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
  UserDO selectById(Long id);
}
