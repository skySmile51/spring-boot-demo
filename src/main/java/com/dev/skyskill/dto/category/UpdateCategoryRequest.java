package com.dev.skyskill.dto.category; // 分类 DTO 包

import jakarta.validation.constraints.Size; // 字段长度校验（字段有值时才校验）
import lombok.Data; // Lombok 数据类

@Data
public class UpdateCategoryRequest { // PATCH /api/category/{id} 请求体，字段均可选

  @Size(min = 1, max = 255, message = "分类名称长度必须在1-255之间") // 若传 name 则校验长度
  private String name; // 待更新的名称

  private String parentId; // 待更新的父级 ID（当前实现未使用，预留）

  private String icon; // 待更新图标

  private String image; // 待更新图片

  private Integer level; // 待更新层级

  private Integer sort; // 待更新排序

  private String description; // 待更新描述
}
