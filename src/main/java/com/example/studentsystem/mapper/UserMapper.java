package com.example.studentsystem.mapper;

import com.example.studentsystem.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper{

    //按照用户名查询用户(把 real_name 映射成 realName)
    @Select("SELECT id, username, password, real_name AS realName, role, created_at AS createdAt FROM user WHERE username = #{username}")
    User findByUsername(String username);

    //根据用户ID查询用户
    @Select("SELECT id, username, password, real_name AS realName, role, created_at AS createdAt FROM user WHERE id = #{id}")
    User findById(Long id);

    //查询所有用户
    @Select("SELECT id, username, password, real_name AS realName, role, created_at AS createdAt FROM user")
    List<User> findAll();

    //新增用户
    @Insert("INSERT INTO user(username, password, real_name, role) VALUES(#{username}, #{password}, #{realName}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    //更新用户密码
    @Update("UPDATE user SET password = #{password} WHERE username = #{username}")
    void updatePassword(@org.apache.ibatis.annotations.Param("username") String username,
                        @org.apache.ibatis.annotations.Param("password") String password);
}