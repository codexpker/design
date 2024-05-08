package com.xpker;

import com.xpker.common.utils.JwtUtil;
import com.xpker.sys.entity.User;
import com.xpker.sys.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserMapper userMapper;

    @Test
    public void createJwt(){
        User user = userMapper.selectById(1);
        System.out.println(user);
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    @Test
    public void parseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwN2ViNDkyOS0zNTUzLTQ0NzQtYjEyYy0wOWZjM2I3M2NiMGEiLCJzdWIiOiJ7XCJwYXNzd29yZFwiOlwiMTIzNDU2XCIsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE3MTUxMzczNTIsImV4cCI6MTcxNTEzOTE1Mn0.HakUcmZm3_g8jy7tq44nolpzOszSQDkEH4k9uuTUI4c";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }


    //重载的方法
    @Test
    public void parseJwt2(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwN2ViNDkyOS0zNTUzLTQ0NzQtYjEyYy0wOWZjM2I3M2NiMGEiLCJzdWIiOiJ7XCJwYXNzd29yZFwiOlwiMTIzNDU2XCIsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE3MTUxMzczNTIsImV4cCI6MTcxNTEzOTE1Mn0.HakUcmZm3_g8jy7tq44nolpzOszSQDkEH4k9uuTUI4c";
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
