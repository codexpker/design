package com.xpker.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xpker.common.utils.JwtUtil;
import com.xpker.common.vo.Result;
import com.xpker.sys.entity.Menu;
import com.xpker.sys.entity.User;
import com.xpker.sys.entity.UserRole;
import com.xpker.sys.mapper.UserMapper;
import com.xpker.sys.mapper.UserRoleMapper;
import com.xpker.sys.service.IMenuService;
import com.xpker.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IMenuService menuService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRoleMapper userRoleMapper;
    //加密后的登录逻辑
    @Override
    public Map<String, Object> login(User user) {
        //根据用户名查询是否登录成功
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);

        //结果不为空且密码匹配，获取token
        if(loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())){
            //暂时先用UUID， 后续改为jwt
//            String key = "user:" + UUID.randomUUID();
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", key);



            //存入redis
            loginUser.setPassword(null);
            String token = jwtUtil.createToken(loginUser);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
//            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
            return data;
        }
        return null;
    }

//    @Override
//    public Map<String, Object> login(User user) {
//        //根据用户名和密码查询是否登录成功
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        wrapper.eq(User::getPassword, user.getPassword());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//
//        //结果不为空，获取token
//        if(loginUser != null){
//            //暂时先用UUID， 后续改为jwt
//            String key = "user:" + UUID.randomUUID();
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", key);
//
//
//            //存入redis
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
//            return data;
//        }
//        return null;
//    }

    @Override
    public Map<String, Object> getInfo(String token) {
        //根据token获取用户信息
//        Object obj = redisTemplate.opsForValue().get(token);
        User loginUser = null;
        try{
            loginUser = jwtUtil.parseToken(token, User.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(loginUser != null){
            //数据库中存的是键值对形式，先要转化成JSON，然后反序列化出对象
//            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            //根据前端接口传值
            data.put("avatar", loginUser.getAvatar());
            data.put("name", loginUser.getUsername());
            List<String> rolesList = this.baseMapper.selectRoleNameById(loginUser.getId());
            data.put("roles", rolesList);

            //权限设置
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList", menuList);
            return data;
        }
        return null;
    }


    //后续整合spring security需要改动
    @Override
    public void logout(String token) {
        //redisTemplate.delete(token);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        //写入用户表
        this.baseMapper.insert(user);
        //写入角色表
        List<Integer> roleIdList = user.getRoleIdList();
        //非空判断
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    @Transactional
    public User getUserById(Integer id) {
        //查询用户表的相关信息
        User user = this.baseMapper.selectById(id);
        //查询角色
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        List<Integer> roleIdList = userRoles.stream()
                                            .map(userRole -> {return userRole.getRoleId();})
                                            .collect(Collectors.toList());
//        List<Integer> roleIdList = new ArrayList<>();
//        if(userRoles != null){
//            for (UserRole userRole : userRoles) {
//                roleIdList.add(userRole.getRoleId());
//            }
//        }
        user.setRoleIdList(roleIdList);
        return user;
    }

    @Override
    @Transactional
    public void updateByUser(User user) {
        List<Integer> roleIdList = user.getRoleIdList();
        //删除有关映射表的信息
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(wrapper);
        //写入映射表
        if(roleIdList != null){
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
        //修改用户表
        this.baseMapper.updateById(user);
    }

    @Override
    public void deleteById(Integer id) {
        //删除关联表的信息
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
        //删除用户表的信息
        this.baseMapper.deleteById(id);
    }
}
