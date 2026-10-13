package com.example.studentsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration //告诉Spring Boot，这是一个配置类
@EnableWebSecurity //开启Spring Security的Web安全功能
@EnableMethodSecurity //开启方法级别的权限注解
public class SecurityConfig{

    /**
     * 定义密码加密器
     * 要求：密码加密存储。使用 BCrypt 算法
     * 只要把这个 Bean 交给 Spring，以后存密码、比对密码，Spring 都会自动加密和校验
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置安全过滤链（Security Filter Chain）
     * 这里定义了哪些接口需要登录，哪些不需要，以及如何处理未登录的情况
     */
    @Autowired // 注入 JWT 过滤器
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                //1.关闭 CSRF 防护
                .csrf(csrf -> csrf.disable())

                //2.允许跨域请求(Apifox调试）
                .cors(cors -> cors.configure(http))

                //3.配置接口权限
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll() // 登录接口放行
                        .anyRequest().authenticated() // 其他接口需要认证
                )

                //4.不使用 Session（用 JWT）
                .sessionManagement(session -> session.sessionCreationPolicy(
                        org.springframework.security.config.http.SessionCreationPolicy.STATELESS
                ))

                //5.把JWT过滤器加在默认的用户名密码验证过滤器之前
                .addFilterBefore(jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 把 AuthenticationManager 暴露出来，供 AuthController 使用
     */
    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}