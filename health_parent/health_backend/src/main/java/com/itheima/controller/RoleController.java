package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色设置控制
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return roleService.findPage(queryPageBean);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.add(role, permissionIds, menuIds);
        } catch (Exception e) {
            return new Result(false, "权限添加失败");
        }
        return new Result(true, "权限添加成功");
    }

    // 真实编辑控制
    @RequestMapping("/edit")
    public Result edit (@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try{
            roleService.edit(role, permissionIds, menuIds);
        } catch (Exception e) {
            return new Result(false, "修改角色信息时遇到未知错误");
        }
        return new Result(true, "角色权限修改成功");
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            roleService.delete(id);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    // 编辑回显 主表单
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Role role = roleService.findById(id);
            return new Result(true, "查询角色信息成功", role);
        } catch (Exception e) {
            return new Result(false, "查询角色信息出错了");
        }
    }

    // 编辑回显 次表单
    @RequestMapping("/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(Integer id) {
        try {
            List<Integer> permissionIds = roleService.findPermissionIdsByRoleId(id);
            return new Result(true, "查询角色权限信息成功", permissionIds);
        } catch (Exception e) {
            return new Result(false, "查询角色权限信息出错啦!");
        }
    }

    // 编辑回显 次表单
    @RequestMapping("/findMenuIdsByRoleId")
    public Result findMenuIdsByRoleId(Integer id) {
        try {
            List<Integer> menuIds = roleService.findMenuIdsByRoleId(id);
            return new Result(true, "查询角色菜单信息成功", menuIds);
        } catch (Exception e) {
            return new Result(false, "查询角色菜单信息出错啦!");
        }
    }

    // 查询所有角色信息
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Role> list = roleService.findAll();
            return new Result(true, "查询所有角色信息成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询所有角色信息失败");
        }
    }
}
