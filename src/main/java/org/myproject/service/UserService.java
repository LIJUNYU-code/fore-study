package org.myproject.service;

import org.myproject.dto.UserLoginDTO;
import org.myproject.dto.UserRegisterDTO;
import org.myproject.entity.User;

public interface UserService {


    // - 校验用户名是否已存在
    // - 对密码进行BCrypt加密
    // - 保存用户信息到数据库
    // - 返回注册成功的用户信息(脱敏)
    void register(UserRegisterDTO dto);


    // - 根据用户名查询用户
    // - 校验密码是否匹配
    // - 校验用户状态是否正常
    // - 生成并返回JWT Token
    String login(UserLoginDTO dto);


    // - 查询用户
    // - 对敏感信息脱敏(清除password字段)
    User getUserById(Long id);


    // - 校验用户是否存在
    // - 更新允许修改的字段(nickname, phone, email, avatar)
    void updateUser(Long id, User user);


    // - 校验旧密码是否正确
    // - 对新密码进行BCrypt加密
    // - 更新密码
    void changePassword(Long id, String oldPassword, String newPassword);
}