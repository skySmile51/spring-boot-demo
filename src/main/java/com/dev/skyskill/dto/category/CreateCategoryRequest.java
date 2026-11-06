package com.dev.skyskill.dto.category; // 分类相关请求/响应 DTO 包

import jakarta.validation.constraints.NotBlank; // 非空字符串校验
import jakarta.validation.constraints.Size; // 长度校验
import lombok.Data; // 自动生成 getter/setter

@Data // Lombok 数据类
public class CreateCategoryRequest { // POST /api/category/create 请求体

  @NotBlank(message = "分类名称不能为空") // 名称为必填
  @Size(min = 1, max = 255, message = "分类名称长度必须在1-255之间") // 长度限制与 Node DTO 一致
  private String name; // 分类名称

  private String parentId; // 父级 category_id，空或 "0" 表示一级分类

  private String icon; // 可选图标

  private String image; // 可选图片

  private Integer level; // 可选层级，未传时由服务端按父级计算

  private Integer sort; // 可选排序，默认 0

  private String description; // 可选描述
}
