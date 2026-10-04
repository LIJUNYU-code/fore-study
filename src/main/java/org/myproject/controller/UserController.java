package org.myproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myproject.dto.UserLoginDTO;
import org.myproject.dto.UserRegisterDTO;
import org.myproject.entity.User;
import org.myproject.service.UserService;
import org.myproject.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResultVO<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        log.info("用户注册请求: username={}", dto.getUsername());
        userService.register(dto);
        log.info("用户注册成功: username={}", dto.getUsername());
        return ResultVO.success();
    }

    @PostMapping("/login")
    public ResultVO<String> login(@Valid @RequestBody UserLoginDTO dto) {
        log.info("用户登录请求: username={}", dto.getUsername());
        String token = userService.login(dto);
        log.info("用户登录成功: username={}", dto.getUsername());
        return ResultVO.success(token);
    }

    @GetMapping("/{id}")
    public ResultVO<User> getUserById(@PathVariable Long id) {
        log.info("查询用户请求: id={}", id);
        User user = userService.getUserById(id);
        log.info("查询用户成功: id={}", id);
        return ResultVO.success(user);
    }

    @PutMapping("/{id}")
    public ResultVO<Void> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("更新用户请求: id={}", id);
        userService.updateUser(id, user);
        log.info("更新用户成功: id={}", id);
        return ResultVO.success();
    }

    @PutMapping("/{id}/password")
    public ResultVO<Void> changePassword(@PathVariable Long id,
                                         @RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        log.info("修改密码请求: id={}", id);
        userService.changePassword(id, oldPassword, newPassword);
        log.info("修改密码成功: id={}", id);
        return ResultVO.success();
    }
}