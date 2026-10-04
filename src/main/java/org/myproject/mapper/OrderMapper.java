package org.myproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.myproject.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}