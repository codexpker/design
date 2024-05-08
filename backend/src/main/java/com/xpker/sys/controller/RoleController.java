package com.xpker.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xpker.common.vo.Result;
import com.xpker.sys.entity.Role;
import com.xpker.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = {"角色接口列表"})
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    IRoleService roleService;

    @ApiOperation("角色分页查询")
    @GetMapping("/list")
    public Result<Map<String, Object>> getRole(@RequestParam(value = "roleName", required = false) String roleName,
                                               @RequestParam("pageNo") Integer pageNo,
                                               @RequestParam("pageSize") Integer pageSize){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper();
        //角色名称可为空
        wrapper.eq(StringUtils.hasLength(roleName), Role::getRoleName, roleName);
        //分页条件
        Page<Role> page = new Page<>(pageNo, pageSize);
        //根据分页条件和查询条件搜索
        roleService.page(page, wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("添加角色")
    @PostMapping
    public Result<?> addRole(@RequestBody Role role){
        roleService.addRole(role);
        return Result.success("添加角色成功");
    }

    @ApiOperation("修改角色信息")
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }

    @ApiOperation("根据id获取角色")
    @GetMapping("/{roleId}")
    public Result<Role> getRoleById(@PathVariable("roleId") Integer roleId){
        Role role = roleService.getRoleById(roleId);
        return Result.success(role);
    }

    @ApiOperation("根据id修改用户")
    @DeleteMapping("/{roleId}")
    public Result<?> deleteRoleById(@PathVariable("roleId") Integer roleId){
        roleService.removeRoleById(roleId);
        return Result.success("删除角色成功");
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> list = roleService.list();
        return Result.success(list);
    }
}
