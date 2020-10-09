package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Menu;
import com.itheima.service.CheckGroupService;
import com.itheima.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 体检检查组控制
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    // 查询所有菜单信息
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Menu> list = menuService.findAll();
            return new Result(true, "获取菜单信息成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "获取菜单信息成功");
        }
    }
}
