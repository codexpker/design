package com.xpker.sys.controller;

import com.xpker.common.vo.Result;
import com.xpker.sys.entity.Menu;
import com.xpker.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
@Api(tags = {"菜单接口列表"})
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    IMenuService menuService;

    @ApiOperation("获取所有菜单列表")
    @GetMapping
    Result<List<Menu>> getAllMenu(){
        List<Menu> list = menuService.getAllMenu();
        return Result.success(list);
    }
}
