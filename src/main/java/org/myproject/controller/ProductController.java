package org.myproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myproject.dto.ProductCreateDTO;
import org.myproject.entity.Product;
import org.myproject.service.ProductService;
import org.myproject.vo.ProductVO;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResultVO<Product> createProduct(@Valid @RequestBody ProductCreateDTO dto) {
        // TODO: 调用productService.createProduct(dto)
        // 返回 ResultVO.success(product)
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/{id}")
    public ResultVO<ProductVO> getProductById(@PathVariable Long id) {
        // TODO: 调用productService.getProductById(id)
        // 返回 ResultVO.success(productVO)
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping("/list")
    public ResultVO<List<ProductVO>> listProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // TODO: 调用productService.listProducts(categoryId, keyword, page, size)
        // 返回 ResultVO.success(productVOList)
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/{id}")
    public ResultVO<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductCreateDTO dto) {
        // TODO: 调用productService.updateProduct(id, dto)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }

    @PutMapping("/{id}/status")
    public ResultVO<Void> toggleStatus(@PathVariable Long id) {
        // TODO: 调用productService.toggleProductStatus(id)
        // 返回 ResultVO.success()
        throw new UnsupportedOperationException("TODO");
    }
}