package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;

import java.util.List;

public interface PermissionSerivce {

    PageResult pageQuery(QueryPageBean queryPageBean);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    void delete(Integer id);

    List<Permission> findAll();
}
