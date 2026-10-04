package org.myproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.myproject.dto.ProductCreateDTO;
import org.myproject.entity.Product;
import org.myproject.mapper.ProductMapper;
import org.myproject.service.ProductService;
import org.myproject.vo.ProductVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Product createProduct(ProductCreateDTO dto) {
        // TODO: 实现创建商品
        // 1. 校验分类是否存在
        // 2. DTO转Entity
        // 3. 设置默认值(status=1, sales=0)
        // 4. 调用productMapper.insert()
        throw new UnsupportedOperationException("TODO: 请实现创建商品");
    }

    @Override
    public ProductVO getProductById(Long id) {
        // TODO: 实现查询商品详情
        // 1. 调用productMapper.selectById()
        // 2. 关联查询分类名称
        // 3. 转换为ProductVO返回
        throw new UnsupportedOperationException("TODO: 请实现查询商品详情");
    }

    @Override
    public List<ProductVO> listProducts(Long categoryId, String keyword, Integer page, Integer size) {
        // TODO: 实现分页查询商品列表
        // 1. 构建LambdaQueryWrapper
        // 2. 如果categoryId不为空, 添加分类筛选条件
        // 3. 如果keyword不为空, 添加名称模糊搜索条件
        // 4. 只查询上架商品(status=1)
        // 5. 按createTime降序排序
        // 6. 使用Page分页查询
        // 7. 转换为ProductVO列表
        throw new UnsupportedOperationException("TODO: 请实现分页查询商品列表");
    }

    @Override
    public void updateProduct(Long id, ProductCreateDTO dto) {
        // TODO: 实现更新商品
        // 1. 校验商品是否存在
        // 2. DTO转Entity, 设置id
        // 3. 调用productMapper.updateById()
        throw new UnsupportedOperationException("TODO: 请实现更新商品");
    }

    @Override
    public void toggleProductStatus(Long id) {
        // TODO: 实现商品上下架
        // 1. 查询商品当前状态
        // 2. 切换状态: 0→1 或 1→0
        // 3. 更新状态
        throw new UnsupportedOperationException("TODO: 请实现商品上下架");
    }

    @Override
    public boolean reduceStock(Long productId, Integer quantity) {
        // TODO: 实现扣减库存(防止超卖)
        // 方案1(乐观锁): UPDATE t_product SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}
        // 方案2(MyBatis-Plus): 使用LambdaUpdateWrapper设置条件
        // 返回影响行数 > 0 表示成功
        throw new UnsupportedOperationException("TODO: 请实现扣减库存");
    }

    @Override
    public void increaseSales(Long productId, Integer quantity) {
        // TODO: 实现增加销量
        // UPDATE t_product SET sales = sales + #{quantity} WHERE id = #{id}
        throw new UnsupportedOperationException("TODO: 请实现增加销量");
    }
}