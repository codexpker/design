package com.xpker.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xpker.sys.entity.Menu;
import com.xpker.sys.mapper.MenuMapper;
import com.xpker.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenu() {
        //获取一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = this.list(wrapper);
        //填充子菜单
        setMenuChildren(menuList);
        return menuList;
    }

    private void setMenuChildren(List<Menu> menuList) {
        if(menuList != null){
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subWrapper = new LambdaQueryWrapper();
                subWrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> subList = this.list(subWrapper);
                menu.setChildren(subList);
                setMenuChildren(subList);
            }
        }
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        //获取一级菜单
        List<Menu> menuList = menuMapper.getMenuListByUserId(userId, 0);
        //填充子菜单
        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if(menuList != null){
            for (Menu menu : menuList) {
                List<Menu> subMenuList = menuMapper.getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                //递归
                setMenuChildrenByUserId(userId, subMenuList);
            }
        }
    }


}
