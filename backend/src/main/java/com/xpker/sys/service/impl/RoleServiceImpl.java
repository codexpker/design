package com.xpker.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xpker.sys.entity.Role;
import com.xpker.sys.entity.RoleMenu;
import com.xpker.sys.mapper.RoleMapper;
import com.xpker.sys.mapper.RoleMenuMapper;
import com.xpker.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    RoleMenuMapper roleMenuMapper;


    @Override
    @Transactional
    public void addRole(Role role) {
        //存入角色表
        this.baseMapper.insert(role);
        //写入角色菜单表
        if(role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }


    @Override
    @Transactional
    public Role getRoleById(Integer roleId) {
        //获取Role
        Role role = this.baseMapper.selectById(roleId);
        //设置menuList
        List<Integer> menuIdList = roleMenuMapper.getMenuIdByRoleId(roleId);
        role.setMenuIdList(menuIdList);
        return role;
    }


    @Override
    @Transactional
    public void updateRole(Role role) {
        //修改role
        this.baseMapper.updateById(role);
        //删除有关该角色的权限列表
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //新增该角色的权限列表
        if(role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public void removeRoleById(Integer roleId) {
        //根据roleId删除角色表
        this.baseMapper.deleteById(roleId);
        //删除关联表
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
    }
}
