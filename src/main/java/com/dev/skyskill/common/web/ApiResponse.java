package com.dev.skyskill.common.web;

import com.dev.skyskill.dao.entity.CategoryDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

  private int code;
  private boolean success;
  private String message;
  private T data;

  public static <T> ApiResponse<T> ok( T data) {
    return new ApiResponse<>(200, true, "成功", data);
  }

  public static <T> ApiResponse<T> fail(String message) {
    return new ApiResponse<>(200, false, message, null);
  }

  public static <T> ApiResponse<T> of(int code, boolean success, String message, T data) {
    return new ApiResponse<>(code, success, message, data);
  }
}
