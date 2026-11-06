package com.dev.skyskill.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dev.skyskill.dao.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/** 查询方法直接用 {@link BaseMapper} 自带的 {@code selectById(Serializable)}，勿在此重复声明。 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
  UserDO selectById(long l);
}
