package org.myproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.myproject.entity.Category;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}