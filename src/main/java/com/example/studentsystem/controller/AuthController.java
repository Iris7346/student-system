package com.example.studentsystem.controller;

import com.example.studentsystem.common.JwtUtil;
import com.example.studentsystem.common.Result;
import com.example.studentsystem.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController //声明这是一个返回 JSON 格式的控制器
@RequestMapping("/api/auth") //接口前缀
public class AuthController{

    private final AuthenticationManager authenticationManager;

    //构造器注入（这个可以保证依赖不可变且不为空）
    public AuthController(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    /**
     * 用户登录接口
     * 请求方式：POST
     * 路径：/api/auth/login
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData){
        //1.从前端传来的 JSON 中提取 username 和 password
        String username = loginData.get("username");
        String password = loginData.get("password");

        //2.使用 AuthenticationManager 进行认证
        //调用 UserDetailsServiceImpl 去数据库查人，并比对密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        //3.如果认证成功，authentication.getPrincipal() 即为查出来的 User 对象
        User user = (User) authentication.getPrincipal();

        //4.生成 JWT Token
        String token = JwtUtil.generateToken(user.getId(), user.getRole());

        //5.组装返回数据
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("role", user.getRole());
        responseData.put("realName", user.getRealName());

        return Result.success(responseData);
    }
}