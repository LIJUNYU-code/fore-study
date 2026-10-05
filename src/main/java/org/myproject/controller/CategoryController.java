package org.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.myproject.entity.Category;
import org.myproject.service.CategoryService;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResultVO<Category> createCategory(@RequestBody Category category) {

        Category category1 = categoryService.createCategory(category);
        // 返回 ResultVO.success(category1)
        return ResultVO.success(category1);
    }

    @GetMapping("/tree")
    public ResultVO<List<Category>> listCategoryTree() {

        List<Category> categoryTree = categoryService.listCategoryTree();
        // 返回 ResultVO.success(categoryTree)
        return ResultVO.success(categoryTree);
    }

    @PutMapping("/{id}")
    public ResultVO<Void> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        // TODO: 调用categoryService.updateCategory(id, category)
        categoryService.updateCategory(id, category);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> deleteCategory(@PathVariable Long id) {
       categoryService.deleteCategory(id);
        // 返回 ResultVO.success()
        return ResultVO.success();
    }
}