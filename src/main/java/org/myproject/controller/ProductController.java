package org.myproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myproject.dto.ProductCreateDTO;
import org.myproject.entity.Product;
import org.myproject.service.ProductService;
import org.myproject.vo.PageResult;
import org.myproject.vo.ProductVO;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 创建商品
     */
    @PostMapping
    public ResultVO<Product> createProduct(@Valid @RequestBody ProductCreateDTO dto) {

        Product product = productService.createProduct(dto);

        return ResultVO.success(product);
    }

    @GetMapping("/{id}")
    public ResultVO<ProductVO> getProductById(@PathVariable Long id) {
        ProductVO productVO = productService.getProductById(id);
        return ResultVO.success(productVO);
    }

    @GetMapping("/list")
    public ResultVO<PageResult<ProductVO>> listProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ProductVO> pageResult = productService.listProducts(categoryId, keyword, page, size);
        return ResultVO.success(pageResult);
    }

    @PutMapping("/{id}")
    public ResultVO<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductCreateDTO dto) {

        productService.updateProduct(id, dto);
        // 返回 ResultVO.success()
        return ResultVO.success();

    }

    @PutMapping("/{id}/status")
    public ResultVO<Void> toggleStatus(@PathVariable Long id) {
        // 返回 ResultVO.success()
        productService.toggleProductStatus(id);
        return ResultVO.success();
    }
}