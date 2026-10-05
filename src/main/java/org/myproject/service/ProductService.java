package org.myproject.service;

import org.myproject.dto.ProductCreateDTO;
import org.myproject.entity.Product;
import org.myproject.vo.PageResult;
import org.myproject.vo.ProductVO;

public interface ProductService {

   /**
    * 创建商品
    * @param dto 商品创建DTO
    * @return 创建的商品商品实体
    */
    Product createProduct(ProductCreateDTO dto);

    /**
    * 根据ID查询商品详情(返回VO, 包含分类名称)
    * @param id 商品ID
    * @return 商品详情VO
    */
    ProductVO getProductById(Long id);

   /**
    * 分页查询商品列表
    * @param categoryId 分类ID(可选)
    * @param keyword 名称模糊搜索(可选)
    * @param page 页码(默认1)
    * @param size 每页数量(默认10)
    * @return 分页结果(包含records、total、page、size、pages)
    */
   PageResult<ProductVO> listProducts(Long categoryId, String keyword, Integer page, Integer size);

    /**
    * 更新商品信息
    * @param id 商品ID
    * @param dto 商品更新DTO
    */
    void updateProduct(Long id, ProductCreateDTO dto);

    /**
    * 商品上下架
    * @param id 商品ID
    */
    void toggleProductStatus(Long id);

    /**
    * 扣减库存(用于下单)
    * @param productId 商品ID
    * @param quantity 扣减数量
    * @return 是否扣减成功
    */
    boolean reduceStock(Long productId, Integer quantity);

    /**
    * 增加销量
    * @param productId 商品ID
    * @param quantity 增加数量
    */
    void increaseSales(Long productId, Integer quantity);
}