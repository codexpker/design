package com.xpker.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xpker.common.vo.Result;
import com.xpker.sys.entity.User;
import com.xpker.sys.mapper.UserMapper;
import com.xpker.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */

@Api(tags = {"用户接口列表"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user){
        Map<String, Object> data = userService.login(user);
        if(data != null){
            return Result.success(data);
        }else{
            return Result.fail(20002, "用户名或密码错误");
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> getInfo(@RequestParam("token") String token){
        Map<String, Object> data = userService.getInfo(token);
        if(data != null){
            return Result.success(data);
        }else{
            return Result.fail(20003, "登录信息无效，请重新登录");
        }
    }

    @ApiOperation("用户注销")
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success("注销成功");
    }

    @ApiOperation("用户分页查询")
    @GetMapping("/list")
    public Result<Map<String, Object>> getUser(@RequestParam(value = "username", required = false) String username,
                                               @RequestParam(value = "phone", required = false) String phone,
                                               @RequestParam("pageNo") Integer pageNo,
                                               @RequestParam("pageSize") Integer pageSize
    ){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //可以为空
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        wrapper.eq(StringUtils.hasLength(phone), User::getPhone, phone);
        //降序排列，这样就可以让新添加的数据在上面
        wrapper.orderByDesc(User::getId);
        //分页条件
        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("添加用户")
    @PostMapping
    public Result<?> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return Result.success("添加用户成功");
    }

    @ApiOperation("修改用户信息")
    @PutMapping
    public Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);
        //updateById如果传入的值为null，则不会更新该字段
        userService.updateByUser(user);
        return Result.success("修改用户成功");
    }

    @ApiOperation("根据id获取用户信息")
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    @ApiOperation("根据id删除用户")
    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable("id") Integer id){
        userService.deleteById(id);
        return Result.success("删除用户成功");
    }
}
