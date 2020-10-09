package com.itheima.dao;



import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);


    Page<Permission> selectByCondition(String queryString);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    long findCountByPermission(Integer id);

    void deleteById(Integer id);

    List<Permission> findAll();

}
