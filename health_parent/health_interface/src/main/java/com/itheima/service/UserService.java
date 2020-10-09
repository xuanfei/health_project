package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.User;

import java.util.List;

public interface UserService {
    public User findByUsername(String username);

    PageResult findPage(QueryPageBean queryPageBean);

    void add(User user, Integer[] roleIds);

    void delete(Integer id);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user, Integer[] roleIds);
}
