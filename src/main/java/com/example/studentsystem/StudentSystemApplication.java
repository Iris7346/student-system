package com.example.studentsystem;

import com.example.studentsystem.entity.User;
import com.example.studentsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class StudentSystemApplication{

    public static void main(String[] args){
        SpringApplication.run(StudentSystemApplication.class, args);
    }

    //这个组件会在项目启动完成后自动运行一次
   // @Component
    public static class PasswordFixer implements CommandLineRunner{
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception{
            System.out.println("正在重置所有用户密码为 123456 ...");

            //1.查出数据库里所有用户
            List<User> allUsers = userMapper.findAll(); //调用之前的 findAll()

            //2.统一加密 123456
            String encoded = passwordEncoder.encode("123456");

            //3.遍历所有人，统一更新密码
            for(User user : allUsers){
                userMapper.updatePassword(user.getUsername(), encoded);
            }

            System.out.println("重置完成！现在所有账号的密码都是 123456 了");
        }
    }
}
