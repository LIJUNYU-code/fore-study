package org.myproject.service;

import org.myproject.dto.ProductCreateDTO;
import org.myproject.entity.Product;
import org.myproject.vo.ProductVO;

import java.util.List;

public interface ProductService {

    // TODO: 创建商品
    // - 校验分类是否存在
    // - DTO转Entity并保存
    Product createProduct(ProductCreateDTO dto);

    // TODO: 根据ID查询商品详情(返回VO, 包含分类名称)
    ProductVO getProductById(Long id);

    // TODO: 分页查询商品列表
    // - 支持按分类ID筛选
    // - 支持按名称模糊搜索
    // - 支持按价格排序
    // - 返回分页结果
    List<ProductVO> listProducts(Long categoryId, String keyword, Integer page, Integer size);

    // TODO: 更新商品信息
    void updateProduct(Long id, ProductCreateDTO dto);

    // TODO: 商品上下架
    // - 切换status: 0-下架 1-上架
    void toggleProductStatus(Long id);

    // TODO: 扣减库存(用于下单)
    // - 使用乐观锁或行锁防止超卖
    // - 返回是否扣减成功
    boolean reduceStock(Long productId, Integer quantity);

    // TODO: 增加销量
    void increaseSales(Long productId, Integer quantity);
}