package org.myproject.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.myproject.entity.Cart;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    Cart selectByUserIdAndProductId(Long userId, Long productId);

    void updateBatchById(LambdaUpdateWrapper<Cart> eq);
}