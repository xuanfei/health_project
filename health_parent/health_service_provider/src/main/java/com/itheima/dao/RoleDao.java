package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer userId);

    Page<Role> selectByCondition(String queryString);

    void setRoleAndPermission(HashMap<String, Integer> map);

    void add(Role role);

    void deleteAssoication(Integer id);

    void deleteByRoleId(Integer id);

    Role findById(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    void edit(Role role);

    List<Role> findAll();

    void setRoleAndMenu(HashMap<String, Integer> map);

    void deleteAssoicationOfMenu(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);
}
