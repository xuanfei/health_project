package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionSerivce;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限设置控制
 */
@RequestMapping("/permission")
@RestController
public class PermissionController {

    @Reference
    private PermissionSerivce permissionSerivce;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return permissionSerivce.pageQuery(queryPageBean);
    }

    // 新增功能
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionSerivce.add(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新建权限项失败");
        }
        return new Result(true, "新建权限项成功");
    }

    // 回显用,通过id查找数据
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Permission permission = permissionSerivce.findById(id);
            return new Result(true, "权限数据查找成功", permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "权限数据查找失败");
        }
    }

    // 真实修改功能
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionSerivce.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "权限修改遇到了问题");
        }
        return new Result(true, "权限信息修改完成!");
    }

    // 删除
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            permissionSerivce.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败, 未知原因");
        }
        return new Result(true, "删除成功!");
    }

    // 查询所有项信息
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Permission> list = permissionSerivce.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
