package org.myproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.myproject.entity.SeckillActivity;

@Mapper
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {

    @Update("UPDATE t_seckill_activity SET available_stock = available_stock - 1 WHERE id = #{id} AND available_stock > 0")
    int decreaseStock(@Param("id") Long id);
}