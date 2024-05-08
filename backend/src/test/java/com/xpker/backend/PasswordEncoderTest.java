package com.xpker.backend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xpker.sys.entity.User;
import com.xpker.sys.mapper.UserMapper;
import com.xpker.sys.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
@SpringBootTest
public class PasswordEncoderTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void test(){
        String username = "admin";
        String password = "123456";
        User user = userMapper.selectById(24);
        System.out.println(user);
        PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder();

        String encodePassword = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println(matches);
    }
}
