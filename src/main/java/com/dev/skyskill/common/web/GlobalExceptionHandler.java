package com.dev.skyskill.common.web; // 全局 Web 异常处理

import com.dev.skyskill.common.exception.BadRequestException; // 400 业务异常
import com.dev.skyskill.common.exception.UnauthorizedException; // 401 未授权
import org.springframework.http.HttpStatus; // HTTP 状态码枚举
import org.springframework.http.ResponseEntity; // 可携带状态码与 body 的响应
import org.springframework.web.bind.MethodArgumentNotValidException; // @Valid 校验失败
import org.springframework.web.bind.annotation.ExceptionHandler; // 标记异常处理方法
import org.springframework.web.bind.annotation.RestControllerAdvice; // 全局 REST Advice

@RestControllerAdvice // 拦截所有 @RestController 抛出的异常
public class GlobalExceptionHandler { // 统一错误响应格式为 ApiResponse

  @ExceptionHandler(BadRequestException.class) // 捕获 BadRequestException
  public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.OK) // HTTP 200，业务码在 body.code
        .body(ApiResponse.of(400, false, ex.getMessage(), null)); // success=false, code=400
  }

  @ExceptionHandler(UnauthorizedException.class) // JWT 等鉴权失败
  public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.of(401, false, ex.getMessage(), null)); // 业务未授权 401
  }

  @ExceptionHandler(MethodArgumentNotValidException.class) // 请求体/参数校验失败
  public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
    String message = // 取第一个字段错误提示
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : "参数校验失败")
            .orElse("参数校验失败");
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.of(400, false, message, null)); // 校验失败视为 400
  }
}
