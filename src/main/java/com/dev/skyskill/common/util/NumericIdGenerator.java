package com.dev.skyskill.common.util; // 工具类包

import java.security.SecureRandom; // 密码学安全的随机数，用于生成业务 ID

/** 生成 13 位纯数字 ID（与 Node 项目 generateNumericId 行为一致） */
public final class NumericIdGenerator { // final 禁止被继承

  private static final long NUMERIC_ID_MIN = 1_000_000_000_000L; // 13 位数字下限
  private static final long NUMERIC_ID_MAX = 9_999_999_999_999L; // 13 位数字上限
  private static final SecureRandom RANDOM = new SecureRandom(); // 全局随机源，线程安全

  private NumericIdGenerator() {} // 私有构造，禁止实例化工具类

  /** 生成一段 13 位纯数字字符串 */
  public static String generate() {
    long value = // 在 [MIN, MAX) 区间内取随机 long
        NUMERIC_ID_MIN + (long) (RANDOM.nextDouble() * (NUMERIC_ID_MAX - NUMERIC_ID_MIN));
    return String.valueOf(value); // 转为字符串供 categoryId 拼接使用
  }
}
