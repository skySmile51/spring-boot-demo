package com.dev.skyskill.dao.mapper; // MyBatis Mapper 接口包

import com.baomidou.mybatisplus.core.mapper.BaseMapper; // MP 内置 CRUD 能力
import com.dev.skyskill.dao.entity.CategoryDO; // 操作的实体类型
import org.apache.ibatis.annotations.Mapper; // 标记为 MyBatis 映射接口

@Mapper // Spring 扫描并注册为 Bean
public interface CategoryMapper extends BaseMapper<CategoryDO> {} // 继承后具备 insert/select/update/delete 等方法
