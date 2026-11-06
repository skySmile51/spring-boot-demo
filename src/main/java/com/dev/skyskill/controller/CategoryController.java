package com.dev.skyskill.controller; // Web 控制器层

import com.dev.skyskill.common.auth.RequireJwt; // 类/方法级 JWT 校验注解
import com.dev.skyskill.common.web.ApiResponse; // 统一 JSON 响应结构
import com.dev.skyskill.dao.entity.CategoryDO; // 分类实体（create/findOne 返回）
import com.dev.skyskill.dto.category.CategoryVo; // 树形列表节点
import com.dev.skyskill.dto.category.CreateCategoryRequest; // 创建 DTO
import com.dev.skyskill.dto.category.UpdateCategoryRequest; // 更新 DTO
import com.dev.skyskill.service.CategoryService; // 业务服务
import jakarta.validation.Valid; // 触发 Bean Validation
import java.util.List; // 树形列表类型
import org.springframework.validation.annotation.Validated; // 启用方法参数校验
import org.springframework.web.bind.annotation.*;

@RestController // 组合 @Controller + @ResponseBody
@Validated // 支持 @Valid 校验
@RequireJwt // 本 Controller 所有接口需携带有效 JWT（与 Node @UseJwtAuth 一致）
@RequestMapping("/category") // 基础路径，对应 Node globalPrefix api + category
public class CategoryController { // 商品分类 HTTP 接口

  private final CategoryService categoryService; // 注入分类服务

  public CategoryController(CategoryService categoryService) { // 构造器注入
    this.categoryService = categoryService;
  }

  @PostMapping("/create") // POST /api/category/create
  public ApiResponse<CategoryDO> create(@Valid @ModelAttribute CreateCategoryRequest request) {
    return ApiResponse.ok(categoryService.create(request)); // 委托 Service，返回带 message 的 ApiResponse
  }

  @GetMapping("/list") // GET /api/category/list（须在 /{id} 之前定义，避免 list 被当成 id）
  public ApiResponse<List<CategoryVo>> findAll() {
    List<CategoryVo> categories = categoryService.findAllTree(); // 查库并组树
    return ApiResponse.ok(categories); // 与 Node 列表 message 一致
  }

  @GetMapping("/{id}") // GET /api/category/{categoryId}
  public CategoryDO findOne(@PathVariable("id") String id) { // id 为 category_id 字符串
    return categoryService.findOne(id); // 直接返回实体 JSON（与 Node findOne 一致，无 ApiResponse 包装）
  }

  @PatchMapping("/{id}") // PATCH /api/category/{数字主键 id}
  public String update(
      @PathVariable("id") Long id, // 路径为表主键 id（与 Node +id 一致）
      @Valid @RequestBody UpdateCategoryRequest request) { // 部分字段更新体
    return categoryService.update(id, request); // 当前为占位字符串
  }

  @DeleteMapping("/{id}") // DELETE /api/category/{数字主键 id}
  public String remove(@PathVariable("id") Long id) { // 表主键
    return categoryService.remove(id); // 当前为占位字符串
  }
}
