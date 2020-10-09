package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.CheckGroupService;
import com.itheima.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/getUsername")
    public Result getUsername() {
        // 当框架完成认证，会保存用户信息到框架提供的上下文对象，底层为session
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user == null) {
            return new Result(true, MessageConstant.GET_USERNAME_FAIL);
        }
        String username = user.getUsername();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return userService.findPage(queryPageBean);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody User user, Integer[] roleIds) {
        try {
            userService.add(user, roleIds);
        } catch (Exception e) {
            return new Result(false, "用户添加失败");
        }
        return new Result(true, "用户添加成功");
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            return new Result(false, "删除用户信息失败");
        }
        return new Result(true, "删除用户信息成功");
    }

    // 编辑回显 form
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            User user = userService.findById(id);
            return new Result(true, "显示成功", user);
        } catch (Exception e) {
            return new Result(false, "信息显示失败");
        }
    }

    // 编辑回显 findRoleIdsByUserId
    @RequestMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(Integer id) {
        try {
            List<Integer> roleIds = userService.findRoleIdsByUserId(id);
            return new Result(true, "获取角色信息成功", roleIds);
        } catch (Exception e) {
            return new Result(false, "获取角色信息失败");
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody User user, Integer[] roleIds) {
        try {
            userService.edit(user, roleIds);
        } catch (Exception e) {
            return new Result(false, "设置用户信息失败");
        }
        return new Result(true, "设置用户信息成功");
    }
}
