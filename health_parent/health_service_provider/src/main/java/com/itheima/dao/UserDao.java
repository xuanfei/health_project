package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.User;

import java.util.HashMap;
import java.util.List;

public interface UserDao {
    User findByUsername(String username);

    Page<User> selectByCondition(String queryString);

    void setUserAndRole(HashMap<String, Integer> map);

    void add(User user);

    void deleteAssoication(Integer id);

    void deleteByUserId(Integer id);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user);
}
