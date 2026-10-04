package org.myproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.myproject.dto.UserLoginDTO;
import org.myproject.dto.UserRegisterDTO;
import org.myproject.entity.User;
import org.myproject.mapper.UserMapper;
import org.myproject.service.UserService;
import org.myproject.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public void register(UserRegisterDTO dto) {

        // 1. 根据username查询数据库, 判断用户名是否已存在
        User existingUser = userMapper.selectByUserName(dto.getUsername());
        // 2. 根据phone查询数据库, 判断手机号是否已存在
        User existingPhoneUser = userMapper.selectByPhone(dto.getPhone());
        // 3. 使用BCryptPasswordEncoder对密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        // 4. 将DTO转换为User实体
        User user = User.builder()
                .username(dto.getUsername())
                .password(encryptedPassword)
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .status(1)
                .build();
        // 5. 调用userMapper.insert()保存
        userMapper.insert(user);
    }

    @Override
    public String login(UserLoginDTO dto) {

        // 1. 根据username查询用户
        User user = userMapper.selectByUserName(dto.getUsername());
        // 2. 判断用户是否存在
        if (user == null) {
            throw new IllegalArgumentException("用户名不存在");
        }
        // 3. 使用BCryptPasswordEncoder校验密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }
        // 4. 判断用户状态是否正常(status == 1)
        if (user.getStatus() != 1) {
            throw new IllegalArgumentException("用户状态异常");
        }
        // 5. 生成JWT Token并返回
        return jwtUtil.generateToken(user.getId().toString(), user.getUsername());
    }

    @Override
    public User getUserById(Long id) {

        // 1. 调用userMapper.selectById()
        User user = userMapper.selectById(id);
        // 2. 清除password字段(脱敏)
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateUser(Long id, User user) {

        // 1. 校验用户是否存在
        User user1 = userMapper.selectById(id);
        if (user1 == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 2. 只更新允许修改的字段: nickname, phone, email, avatar
        user1.setNickname(user.getNickname());
        user1.setPhone(user.getPhone());
        user1.setEmail(user.getEmail());
        user1.setAvatar(user.getAvatar());

        // 3. 调用userMapper.updateById()
        userMapper.updateById(user1);
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {

        // 1. 查询当前用户
        User user = userMapper.selectById(id);
        // 2. 校验旧密码是否正确
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }
        else if (newPassword.equals(oldPassword)) {
            throw new IllegalArgumentException("新密码不能与旧密码相同");
        }
        // 3. 加密新密码
        String encryptedNewPassword = passwordEncoder.encode(newPassword);
        // 4. 更新密码
        user.setPassword(encryptedNewPassword);
        userMapper.updateById(user);
    }
}