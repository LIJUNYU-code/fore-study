package org.myproject.service;

import jakarta.validation.constraints.NotNull;
import org.myproject.entity.Category;

import java.util.List;

public interface CategoryService {

    // TODO: 创建分类
    Category createCategory(Category category);

    // TODO: 查询所有分类(树形结构)
    // - 查询所有分类
    // - 组装父子关系返回树形结构
    List<Category> listCategoryTree();

    // TODO: 更新分类
    void updateCategory(Long id, Category category);

    // TODO: 删除分类
    // - 校验该分类下是否有商品
    // - 如有商品则不允许删除
    void deleteCategory(Long id);

    Category getCategoryById(@NotNull(message = "分类ID不能为空") Long categoryId);
}