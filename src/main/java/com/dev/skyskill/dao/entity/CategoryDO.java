package com.dev.skyskill.dao.entity; // 数据库实体包

import com.baomidou.mybatisplus.annotation.IdType; // 主键策略枚举
import com.baomidou.mybatisplus.annotation.TableField; // 字段与列名映射
import com.baomidou.mybatisplus.annotation.TableId; // 主键标记
import com.baomidou.mybatisplus.annotation.TableName; // 表名映射
import java.time.LocalDateTime; // Java 8 日期时间类型
import lombok.AllArgsConstructor; // 生成全参构造
import lombok.Builder; // 建造者模式
import lombok.Data; // 生成 getter/setter 等
import lombok.NoArgsConstructor; // 生成无参构造

@Data // Lombok：自动生成常用方法
@Builder // Lombok：支持链式构建对象
@NoArgsConstructor // MyBatis-Plus 反序列化/反射需要无参构造
@AllArgsConstructor // 配合 @Builder 生成全参构造
@TableName("category") // 对应 MySQL 表 category
public class CategoryDO { // 商品分类持久化对象

  @TableId(type = IdType.AUTO) // 数据库自增主键 id
  private Long id; // 表主键

  @TableField("category_id") // 列 category_id
  private String categoryId; // 业务分类 ID，如 ca_8000001000001

  @TableField("name")
  private String name; // 分类名称

  @TableField("parent_id")
  private String parentId; // 父级 category_id，根节点为 "0"

  @TableField("icon")
  private String icon; // 图标 URL 或 emoji

  @TableField("image")
  private String image; // 分类展示图

  @TableField("description")
  private String description; // 分类描述

  @TableField("level")
  private Integer level; // 层级：1 一级、2 二级…

  @TableField("sort")
  private Integer sort; // 排序值，越大越靠前

  @TableField("path")
  private String path; // 物化路径，如 /ca_xxx/ca_yyy/

  @TableField("is_deleted")
  private Integer isDeleted; // 逻辑删除：0 否 1 是

  @TableField("is_leaf")
  private Integer isLeaf; // 是否叶子：0 有子节点 1 叶子

  @TableField("status")
  private Integer status; // 状态：1 正常 0 禁用

  @TableField("created_at")
  private LocalDateTime createdAt; // 创建时间

  @TableField("updated_at")
  private LocalDateTime updatedAt; // 更新时间
}
