package org.myproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.myproject.dto.ProductCreateDTO;
import org.myproject.entity.Cart;
import org.myproject.entity.Category;
import org.myproject.entity.Product;
import org.myproject.mapper.ProductMapper;
import org.myproject.service.CategoryService;
import org.myproject.service.ProductService;
import org.myproject.vo.PageResult;
import org.myproject.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductMapper productMapper;
    @Autowired
    private final CategoryService categoryService;

    @Override
    public Product createProduct(ProductCreateDTO dto) {
        // 1. 校验分类是否存在
        Category category = categoryService.getCategoryById(dto.getCategoryId());
        if(category == null){
            throw new IllegalArgumentException("分类不存在");
        }
        // 2. DTO转Entity
        Product product = Product.builder()
                .name(dto.getName())
                .categoryId(dto.getCategoryId())
                .price(dto.getPrice())
                .originalPrice(dto.getOriginalPrice())
                .stock(dto.getStock())
                .sales(0)
                .description(dto.getDescription())
                .image(dto.getImage())
                .status(1)
                .build();
        // 4. 调用productMapper.insert()保存
        productMapper.insert(product);
        return product;
    }

    @Override
    public ProductVO getProductById(Long id) {

        // 1. 调用productMapper.selectById()查询商品实体
        Product product = productMapper.selectById(id);
        // 2. 关联查询分类名称
        Category category = categoryService.getCategoryById(product.getCategoryId());
        if(category == null){
            throw new IllegalArgumentException("分类不存在");
        }
        // 3. 转换为ProductVO返回
        return ProductVO.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .stock(product.getStock())
                .sales(product.getSales())
                .description(product.getDescription())
                .image(product.getImage())
                .status(product.getStatus())
                .categoryName(category.getName())
                .build();
    }

    @Override
    public PageResult<ProductVO> listProducts(Long categoryId, String keyword, Integer page, Integer size) {
        // 1. 构建LambdaQueryWrapper
        LambdaQueryWrapper<Product> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 2. 如果categoryId不为空, 添加分类筛选条件
        if(categoryId != null){
            objectLambdaQueryWrapper.eq(Product::getCategoryId, categoryId);
        }
        // 3. 如果keyword不为空, 添加名称模糊搜索条件
        if(keyword != null && !keyword.isEmpty()){
            objectLambdaQueryWrapper.like(Product::getName, keyword);
        }
        // 4. 只查询上架商品(status=1)
        objectLambdaQueryWrapper.eq(Product::getStatus, 1);
        // 5. 按createTime降序排序
        objectLambdaQueryWrapper.orderByDesc(Product::getCreateTime);
        // 6. 使用Page分页查询
        Page<Product> pages = new Page<>(page, size);
        Page<Product> productPage = productMapper.selectPage(pages, objectLambdaQueryWrapper);

        // 7. 转换为ProductVO列表
        List<ProductVO> productVOList = productPage.getRecords().stream()
                .map(product -> {
                    Category category = categoryService.getCategoryById(product.getCategoryId());
                    return ProductVO.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .categoryId(product.getCategoryId())
                            .price(product.getPrice())
                            .originalPrice(product.getOriginalPrice())
                            .stock(product.getStock())
                            .sales(product.getSales())
                            .description(product.getDescription())
                            .image(product.getImage())
                            .status(product.getStatus())
                            .categoryName(category != null ? category.getName() : "未知分类")
                            .build();
                })
                .toList();

        // 8. 返回分页结果（包含 total 信息）
        return PageResult.of(productVOList, productPage.getTotal(), page, size);
    }


    @Override
    public void updateProduct(Long id, ProductCreateDTO dto) {

        // 1. 校验商品是否存在
        Product product = productMapper.selectById(id);
        if(product == null){
            throw new IllegalArgumentException("商品不存在");
        }
        // 2. DTO转Entity, 设置id
        product = Product.builder()
                .id(id)
                .name(dto.getName())
                .categoryId(dto.getCategoryId())
                .price(dto.getPrice())
                .originalPrice(dto.getOriginalPrice())
                .stock(dto.getStock())
                .sales(product.getSales())
                .description(dto.getDescription())
                .image(dto.getImage())
                .status(product.getStatus())
                .build();
               // 3. 调用productMapper.updateById()更新商品
        productMapper.updateById(product);
    }

    @Override
    public void toggleProductStatus(Long id) {
        // 1. 查询商品当前状态
        Product product = productMapper.selectById(id);
        if(product == null) {
            throw new IllegalArgumentException("商品不存在");
        }
        // 2. 切换状态: 0→1 或 1→0
        product.setStatus(product.getStatus() == 1 ? 0 : 1);
        productMapper.updateById(product);

    }

    /**
     * 删除商品（物理删除）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProduct(Long id) {
        // 1. 校验商品是否存在
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        // 2. 物理删除商品
        productMapper.deleteById(id);
    }

    /**
     * 扣减库存(防止超卖)
     * 使用数据库原子操作，保证并发安全
     */
    @Transactional(rollbackFor = Exception.class)  // 加事务保证原子性
    @Override
    public boolean reduceStock(Long productId, Integer quantity) {
        // 方案：使用单条SQL原子操作扣减库存（推荐）
        // SQL: UPDATE t_product SET stock = stock - #{quantity}
        //     WHERE id = #{id} AND stock >= #{quantity} AND status = 1

        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("stock = stock - " + quantity);   // 原子减法操作
        wrapper.eq(Product::getId, productId);           // 指定商品ID
        wrapper.ge(Product::getStock, quantity);          // 库存必须 >= 购买数量（防止超卖）
        wrapper.eq(Product::getStatus, 1);               // 商品必须是上架状态

        int rows = productMapper.update(null, wrapper);

        if (rows == 0) {
            // 更新失败：商品不存在 或 库存不足 或 已下架
            throw new IllegalArgumentException("库存不足或商品已下架");
        }

        return true;
    }

    /**
     * 增加销量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void increaseSales(Long productId, Integer quantity) {
        // 使用原子操作增加销量（与reduceStock保持一致）
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("sales = sales + " + quantity);   // 原子加法操作
        wrapper.eq(Product::getId, productId);           // 指定商品ID

        int rows = productMapper.update(null, wrapper);

        if (rows == 0) {
            throw new IllegalArgumentException("商品不存在");
        }
    }
    /**
     * 根据购物车项查询商品列表
     */

    @Override
    public List<Product> getProductsByCartItems(List<Cart> cartItems) {
        return productMapper.selectBatchIds(cartItems.stream().map(Cart::getProductId).toList());
    }
}