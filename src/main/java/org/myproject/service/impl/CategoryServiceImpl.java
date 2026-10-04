package org.myproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.myproject.entity.Category;
import org.myproject.mapper.CategoryMapper;
import org.myproject.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public Category createCategory(Category category) {
        // TODO: 实现创建分类
        // 1. 校验父分类是否存在(如果parentId != 0)
        // 2. 调用categoryMapper.insert()
        throw new UnsupportedOperationException("TODO: 请实现创建分类");
    }

    @Override
    public List<Category> listCategoryTree() {
        // TODO: 实现查询分类树
        // 1. 查询所有分类(categoryMapper.selectList)
        // 2. 按parentId分组
        // 3. 递归组装父子关系
        // 4. 返回顶级分类列表(带children)
        // 提示: 可以在Category实体中添加 @TableField(exist=false) List<Category> children
        throw new UnsupportedOperationException("TODO: 请实现查询分类树");
    }

    @Override
    public void updateCategory(Long id, Category category) {
        // TODO: 实现更新分类
        // 1. 校验分类是否存在
        // 2. 设置id
        // 3. 调用categoryMapper.updateById()
        throw new UnsupportedOperationException("TODO: 请实现更新分类");
    }

    @Override
    public void deleteCategory(Long id) {
        // TODO: 实现删除分类
        // 1. 校验该分类下是否有商品(查询t_product WHERE category_id = #{id})
        // 2. 如果有商品, 抛出业务异常
        // 3. 调用categoryMapper.deleteById() (逻辑删除)
        throw new UnsupportedOperationException("TODO: 请实现删除分类");
    }
}