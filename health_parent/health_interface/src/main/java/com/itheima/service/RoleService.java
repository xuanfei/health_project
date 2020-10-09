package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;

import java.util.List;

public interface RoleService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    void delete(Integer id);

    Role findById(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    List<Role> findAll();

    List<Integer> findMenuIdsByRoleId(Integer id);
}
