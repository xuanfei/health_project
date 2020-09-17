package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查组接口
 */
public interface CheckGroupService {
    // 新增功能
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckitemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delete(Integer checkGroupId);
}
