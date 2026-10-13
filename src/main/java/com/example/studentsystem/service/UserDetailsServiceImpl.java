package com.example.studentsystem.service;

import com.example.studentsystem.entity.User;
import com.example.studentsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 这个类的作用：告诉 Spring Security 怎么从数据库里查用户
 * 必须实现 UserDetailsService 接口
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired //自动把UserMapper注入进来（不用自己去new）
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //1.调用Mapper去数据库根据用户名查数据
        User user = userMapper.findByUsername(username);

        //2.如果没查到，抛出异常，Spring Security会自动处理（登录失败）
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        //3.如果查到了，直接返回User对象
        return user;
    }
}