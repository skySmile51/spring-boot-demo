package com.dev.skyskill.common.exception; // 声明当前类所属包，便于统一管理与导入

/** 业务参数或请求不合法时抛出，由全局异常处理器转为 400 响应 */
public class BadRequestException extends RuntimeException { // 继承运行时异常，无需强制捕获

  /** @param message 返回给前端的错误说明 */
  public BadRequestException(String message) { // 构造方法，传入错误信息
    super(message); // 将消息交给父类 RuntimeException 保存
  }
}
