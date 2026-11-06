package com.dev.skyskill.service.impl; // 业务实现类包

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper; // 类型安全的查询条件
import com.dev.skyskill.common.exception.BadRequestException; // 父分类不存在等业务错误
import com.dev.skyskill.common.util.NumericIdGenerator; // 生成分类 ID 数字段
import com.dev.skyskill.common.web.ApiResponse; // 统一响应封装
import com.dev.skyskill.dao.entity.CategoryDO; // 分类实体
import com.dev.skyskill.dao.mapper.CategoryMapper; // 分类 Mapper
import com.dev.skyskill.dto.category.CategoryVo; // 树节点 VO
import com.dev.skyskill.dto.category.CreateCategoryRequest; // 创建请求
import com.dev.skyskill.dto.category.UpdateCategoryRequest; // 更新请求
import com.dev.skyskill.service.CategoryService; // 服务接口
import java.util.ArrayList; // 可变列表
import java.util.List; // 列表类型
import org.springframework.stereotype.Service; // 标记 Spring 服务 Bean
import org.springframework.util.StringUtils; // 字符串工具 hasText

@Service // 注册为 Spring 单例服务
public class CategoryServiceImpl implements CategoryService { // 分类业务实现

  private final CategoryMapper categoryMapper; // 注入数据访问层

  public CategoryServiceImpl(CategoryMapper categoryMapper) { // 构造器注入 Mapper
    this.categoryMapper = categoryMapper; // 保存引用供方法使用
  }

  @Override
  public CategoryDO create(CreateCategoryRequest request) { // 创建分类入口
    String parentId = request.getParentId(); // 读取请求中的父级 ID
    if (StringUtils.hasText(parentId) && !"0".equals(parentId)) { // 有父级且非根：走子级创建分支
      CategoryDO parentCategory = findOne(parentId); // 按 categoryId 查父分类
      if (parentCategory == null) { // 父级不存在
        throw new BadRequestException("父级分类不存在"); // 抛业务异常，全局处理为 400
      }
      String categoryId = "ca_" + NumericIdGenerator.generate(); // 生成新业务 ID
      CategoryDO newCategory = // 组装新分类实体（level 为父级 +1）
          buildCategoryFromRequest(request, categoryId, parentId, parentCategory.getLevel() + 1);
      newCategory.setPath(parentCategory.getPath() + categoryId + "/"); // 物化路径：父 path + 当前 id

      CategoryDO parentUpdate = new CategoryDO(); // 仅更新父节点字段的载体
      parentUpdate.setId(parentCategory.getId()); // 指定父记录主键
      parentUpdate.setIsLeaf(0); // 有子节点后父级不再是叶子
      categoryMapper.updateById(parentUpdate); // 更新父分类 is_leaf
      categoryMapper.insert(newCategory); // 插入子分类

      return newCategory; // 返回成功与新建数据
    }

    String categoryId = "ca_" + NumericIdGenerator.generate(); // 一级分类：生成 categoryId
    CategoryDO newCategory = buildCategoryFromRequest(request, categoryId, "0", 1); // parentId=0，level=1
    newCategory.setPath("/" + categoryId + "/"); // 根路径格式 /ca_xxx/
    categoryMapper.insert(newCategory); // 写入数据库
    return newCategory; // 返回成功响应
  }

  /** 根据创建请求与已算好的 id、parentId、level 构建待插入实体 */
  private static CategoryDO buildCategoryFromRequest(
      CreateCategoryRequest request, // 原始请求
      String categoryId, // 已生成的业务 ID
      String parentId, // 父级 category_id
      int level) { // 默认层级（请求未传 level 时使用）
    return CategoryDO.builder()
        .categoryId(categoryId) // 业务主键
        .name(request.getName()) // 名称
        .parentId(parentId) // 父 ID
        .icon(defaultString(request.getIcon())) // 图标，空则 ""
        .image(defaultString(request.getImage())) // 图片，空则 ""
        .description(request.getDescription()) // 描述可为 null
        .level(request.getLevel() != null ? request.getLevel() : level) // 请求优先，否则用计算 level
        .sort(request.getSort() != null ? request.getSort() : 0) // 默认排序 0
        .isDeleted(0) // 未删除
        .isLeaf(0) // 新建默认非叶子（与 Node 实体默认一致）
        .status(1) // 正常状态
        .build();
  }

  /** null 转空串，避免 icon/image 列 null */
  private static String defaultString(String value) {
    return value != null ? value : ""; // 三元表达式返回非 null 字符串
  }

  @Override
  public List<CategoryVo> findAllTree() { // 查询并组装树
    List<CategoryDO> categories = // 查全表
        categoryMapper.selectList(
            new LambdaQueryWrapper<CategoryDO>().orderByAsc(CategoryDO::getSort)); // 按 sort 升序
    List<CategoryVo> tree = new ArrayList<>(); // 根节点列表
    for (CategoryDO category : categories) { // 遍历每条 flat 记录
      CategoryVo node = CategoryVo.from(category); // DO → VO
      if ("0".equals(category.getParentId())) { // 根节点直接加入 tree
        tree.add(node);
      } else { // 非根：挂到已存在于 tree 中的父节点下
        CategoryVo parentCategory = findParentCategory(tree, category.getParentId()); // DFS 找父 VO
        if (parentCategory != null) { // 找到父节点
          attachChild(parentCategory, node); // 追加到 children
        } else { // 父尚未出现在 tree（数据异常或顺序问题）则当作根
          tree.add(node);
        }
      }
    }
    return tree; // 返回完整树
  }

  /** 在已组好的树中递归查找 categoryId 对应的节点引用 */
  private CategoryVo findParentCategory(List<CategoryVo> tree, String parentId) {
    for (CategoryVo category : tree) { // 遍历当前层
      if (parentId.equals(category.getCategoryId())) { // 命中则返回
        return category;
      }
      if (category.getChildren() != null && !category.getChildren().isEmpty()) { // 有子节点则深入
        CategoryVo found = findParentCategory(category.getChildren(), parentId); // 递归子树
        if (found != null) { // 子树中找到
          return found;
        }
      }
    }
    return null; // 整棵子树未找到
  }

  /** 将 target 加入 parentCategory 的 children 列表 */
  private static void attachChild(CategoryVo parentCategory, CategoryVo target) {
    if (parentCategory.getChildren() == null) { // 防御：children 未初始化
      parentCategory.setChildren(new ArrayList<>()); // 新建子列表
    }
    parentCategory.getChildren().add(target); // 挂载子节点（与 tree 中父节点同一引用）
  }

  @Override
  public CategoryDO findOne(String categoryId) { // 按业务 ID 查单条
    return categoryMapper.selectOne( // MP 查一条
        new LambdaQueryWrapper<CategoryDO>().eq(CategoryDO::getCategoryId, categoryId)); // 条件 category_id
  }

  @Override
  public String update(Long id, UpdateCategoryRequest request) { // 占位：与 Node 一致
    return "This action updates a #" + id + " category"; // 返回说明字符串
  }

  @Override
  public String remove(Long id) { // 占位：与 Node 一致
    return "This action removes a #" + id + " category"; // 返回说明字符串
  }
}
