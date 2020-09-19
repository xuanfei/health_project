package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组数据持久层接口
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(HashMap<String, Integer> map);

    void deleteAssoication(Integer id);

    void deleteByGroupId(Integer id);

    Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckitemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    List<CheckGroup> findAll();

}
