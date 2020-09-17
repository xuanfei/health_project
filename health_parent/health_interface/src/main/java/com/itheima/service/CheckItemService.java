package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项服务接口
 */
public interface CheckItemService {
    // 新增功能
    void add(CheckItem checkItem);

    // 分页查询
    PageResult pageQuery(QueryPageBean queryPageBean);

    // 删除
    void delete(Integer id);

    // 回显
    CheckItem findById(Integer id);

    // 修改
    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
