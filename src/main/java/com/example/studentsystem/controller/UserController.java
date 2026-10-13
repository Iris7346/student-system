package com.example.studentsystem.controller;

import com.example.studentsystem.common.JwtUtil;
import com.example.studentsystem.common.Result;
import com.example.studentsystem.entity.User;
import com.example.studentsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('TEACHER')")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;//注入密码加密器

    //1.查看所有用户(TEACHER)
    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Result<List<User>> getAllUsers() {
        return Result.success(userMapper.findAll());
    }

    //2.创建用户(TEACHER)
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> createUser(@RequestBody User user) {
        //关键安全点：不能把明文密码存进数据库，必须加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //调用 Mapper 的 insert 方法
        userMapper.insert(user);
        return Result.success();
    }

    /**
     * 查看个人信息接口
     * 请求方式：GET
     * 路径：/api/users/profile
     * 注意：这个接口需要登录后才能访问
     */
    @GetMapping("/profile")
    public Result<User> getProfile(@RequestHeader("Authorization") String token) {
        //1.客户端传来的 Token 通常带 "Bearer " 前缀，把它切掉
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        //2.从 Token 里解析出用户ID
        Long userId = JwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error(401, "无效的 Token，请重新登录");
        }

        //3.拿着用户ID去数据库查详细信息(通过 findById 查)
        User user = userMapper.findById(userId);

        //4.出于安全，把密码置空，不返回给前端
        if (user != null) {
            user.setPassword(null);
        }

        return Result.success(user);
    }
}