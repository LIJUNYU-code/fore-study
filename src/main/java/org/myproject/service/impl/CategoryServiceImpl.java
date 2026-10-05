package org.myproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.myproject.entity.Category;
import org.myproject.entity.Product;
import org.myproject.mapper.CategoryMapper;
import org.myproject.mapper.ProductMapper;
import org.myproject.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final ProductMapper productMapper;

    @Override
    public Category createCategory(Category category) {

        // 1. 校验父分类是否存在(如果parentId != 0)
        if (category.getParentId() != null) {
            Category parent = categoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("父分类不存在");
            }
        }
        // 2. 调用categoryMapper.insert()
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public List<Category> listCategoryTree() {
        // TODO: 实现查询分类树
        // 1. 查询所有分类(categoryMapper.selectList)
        List<Category> allCategories = categoryMapper.selectList(null);
        // 2. 按parentId分组
        Map<Long, List<Category>> categoryMap = allCategories.stream()
                .collect(Collectors.groupingBy(Category::getParentId));

        // 3. 递归组装父子关系
        List<Category> topCategories = categoryMap.get(0L);
        //todo
//        // 4. 返回顶级分类列表(带children)
//        topCategories.forEach(category -> {
//            List<Category> children = categoryMap.get(category.getId());
//            if (children != null) {
//                category.setChildren(children);
//            }
//        });
        return topCategories;
    }

    @Override
    public void updateCategory(Long id, Category category) {
        // TODO: 实现更新分类
        // 1. 校验分类是否存在
        Category existingCategory = categoryMapper.selectById(id);
        if (existingCategory == null) {
            throw new IllegalArgumentException("分类不存在");
        }
        // 2. 设置id
        category.setId(id);
        // 3. 调用categoryMapper.updateById()
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        // TODO: 实现删除分类
        // 1. 校验该分类下是否有商品(查询t_product WHERE category_id = #{id})
        Long productCount =  productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getCategoryId, id));
        // 2. 如果有商品, 抛出业务异常
        if (productCount > 0) {
            throw new IllegalArgumentException("分类下有商品, 不能删除");
        }
        // 3. 调用categoryMapper.deleteById() (逻辑删除)
        categoryMapper.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryMapper.selectById(categoryId);
    }
}