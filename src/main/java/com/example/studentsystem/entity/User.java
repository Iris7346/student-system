package com.example.studentsystem.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
public class User implements UserDetails { //实现UserDetails接口
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role; //数据库里存的是STUDENT或TEACHER
    private LocalDateTime createdAt;

    /**
     * 返回用户的权限列表。
     * Spring Security 要求角色名必须以 "ROLE_" 开头。
     * 数据库里存的是 "STUDENT"，这里转换成 "ROLE_STUDENT"
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Collections.singletonList 表示只返回一个元素的列表
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    //以下方法返回true，表示账号没有过期、没有被锁定、密码没有过期、账号已启用
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}