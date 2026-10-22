package com.dev.skyskill.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** 用户表（对应表 users） */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class UserDO {

  /** 主键 ID */
  @TableId(type = IdType.AUTO)
  private Long id;

  /** 用户 ID（业务侧唯一标识） */
  @TableField("user_id")
  private String userId;

  /** 用户名 */
  @TableField("username")
  private String username;

  /** 昵称 */
  @TableField("nickname")
  private String nickname;

  /** 头像 URL */
  @TableField("avatar")
  private String avatar;

  /** 性别 */
  @TableField("gender")
  private Integer gender;

  /** 生日 */
  @TableField("birthday")
  private LocalDate birthday;

  /** 手机号 */
  @TableField("phone")
  private String phone;

  /** 邮箱 */
  @TableField("email")
  private String email;

  /** 状态：0 禁用，1 启用 */
  @TableField("status")
  private Integer status;

  /** 密码（勿在 API 中直接返回） */
  @TableField("password")
  private String password;

  /** 创建时间 */
  @TableField("created_at")
  private LocalDateTime createdAt;

  /** 更新时间 */
  @TableField("updated_at")
  private LocalDateTime updatedAt;
}
