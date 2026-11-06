package com.dev.skyskill.service; // 业务服务接口层

import com.dev.skyskill.dao.entity.CategoryDO; // 分类实体
import com.dev.skyskill.dto.category.CategoryVo; // 树形节点 VO
import com.dev.skyskill.dto.category.CreateCategoryRequest; // 创建请求 DTO
import com.dev.skyskill.dto.category.UpdateCategoryRequest; // 更新请求 DTO
import java.util.List; // 树形列表

/** 商品分类业务接口，与 Node CategoryService 能力对齐 */
public interface CategoryService {

  /**
   * 创建一级或子级分类
   */
  CategoryDO create(CreateCategoryRequest request);

  /** 查询全部分类并组装为树 */
  List<CategoryVo> findAllTree();

  /** 按业务 categoryId 查询单条 */
  CategoryDO findOne(String categoryId);

  /** 更新分类（当前为占位实现） */
  String update(Long id, UpdateCategoryRequest request);

  /** 删除分类（当前为占位实现） */
  String remove(Long id);
}
