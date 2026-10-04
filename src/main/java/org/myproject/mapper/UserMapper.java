package org.myproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Mapper;
import org.myproject.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查询用户
     */
    User selectByUserName(@NotBlank(message = "用户名不能为空") String username);


    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone);

    /**
     * 根据用户ID查询用户
     */
    User selectById(Long id);
}