package com.xpker.sys.service;

import com.xpker.sys.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getAllMenu();
    List<Menu> getMenuListByUserId(Integer userId);
}
