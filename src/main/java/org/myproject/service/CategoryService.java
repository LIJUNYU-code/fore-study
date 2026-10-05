package org.myproject.service;

import jakarta.validation.constraints.NotNull;
import org.myproject.entity.Category;

import java.util.List;

public interface CategoryService {


    /**
     * 创建分类
     * @param category 分类实体
     * @return 创建后的分类实体
     */
    Category createCategory(Category category);

    /**
     * 查询所有分类树形结构
     * @return 分类树形结构列表
     */
    List<Category> listCategoryTree();

    /**
     * 更新分类
     * @param id 分类ID
     * @param category 更新后的分类实体
     */
    void updateCategory(Long id, Category category);


    // - 校验该分类下是否有商品
    // - 如有商品则不允许删除
    void deleteCategory(Long id);

    Category getCategoryById(@NotNull(message = "分类ID不能为空") Long categoryId);
}