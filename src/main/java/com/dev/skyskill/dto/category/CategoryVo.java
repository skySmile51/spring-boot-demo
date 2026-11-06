package com.dev.skyskill.dto.category; // 分类对外展示对象

import com.dev.skyskill.dao.entity.CategoryDO; // 数据库实体，用于转换
import com.fasterxml.jackson.annotation.JsonInclude; // 控制 JSON 序列化是否包含 null
import java.time.LocalDateTime; // 时间字段类型
import java.util.ArrayList; // 子节点列表默认实现
import java.util.List; // 子节点集合类型
import lombok.AllArgsConstructor; // 全参构造
import lombok.Builder; // 建造者
import lombok.Data; // getter/setter
import lombok.NoArgsConstructor; // 无参构造

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 响应 JSON 省略 null 字段
public class CategoryVo { // 列表树形接口返回的节点结构

  private Long id; // 表主键
  private String categoryId; // 业务分类 ID
  private String name; // 名称
  private String parentId; // 父级 category_id
  private String icon; // 图标
  private String image; // 图片
  private String description; // 描述
  private Integer level; // 层级
  private Integer sort; // 排序
  private String path; // 物化路径
  private Integer isDeleted; // 是否删除
  private Integer isLeaf; // 是否叶子
  private Integer status; // 状态
  private LocalDateTime createdAt; // 创建时间
  private LocalDateTime updatedAt; // 更新时间

  @Builder.Default // Builder 构建时若未设置 children 则用默认值
  private List<CategoryVo> children = new ArrayList<>(); // 子分类列表，组成树

  /** 将 DO 转为 VO（不含 children，树结构在 Service 中挂载） */
  public static CategoryVo from(CategoryDO entity) {
    if (entity == null) { // 空实体直接返回 null
      return null;
    }
    return CategoryVo.builder() // 使用建造者拷贝字段
        .id(entity.getId())
        .categoryId(entity.getCategoryId())
        .name(entity.getName())
        .parentId(entity.getParentId())
        .icon(entity.getIcon())
        .image(entity.getImage())
        .description(entity.getDescription())
        .level(entity.getLevel())
        .sort(entity.getSort())
        .path(entity.getPath())
        .isDeleted(entity.getIsDeleted())
        .isLeaf(entity.getIsLeaf())
        .status(entity.getStatus())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build(); // 生成 VO 实例
  }
}
