package com.example.studentsystem.config;

import com.example.studentsystem.common.JwtUtil;
import com.example.studentsystem.entity.User;
import com.example.studentsystem.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 * 作用：在每次 HTTP 请求到达 Controller 之前，拦截它，检查请求头里有没有合法的 Token
 * 如果有，就把用户信息存入 Spring Security 的上下文中
 */
@Component //交给 Spring 管理
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //1.从请求头中获取 Authorization 字段
        String token = request.getHeader("Authorization");

        //2.如果 Token 存在并且以"Bearer "开头，就开始解析
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); //去掉"Bearer "前缀

            //3.解析 Token，获取 userId
            Long userId = JwtUtil.getUserIdFromToken(token);

            //4.如果 userId 有效，并且当前 Security 上下文里没有用户信息（避免重复认证）
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //去数据库查询这个用户
                User user = userMapper.findById(userId);

                if (user != null) {
                    //5.把用户信息封装成 Security 认证对象
                    //注意：这里不校验密码，因为密码在登录时已经校验过了
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    //6.存入 Security 上下文，表示“这个用户已经登录了”
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        //7.放行请求(到达 Controller）
        filterChain.doFilter(request, response);
    }
}