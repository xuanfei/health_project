package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

/**
 * 检查项数据持久层接口
 */
public interface CheckItemDao {

    // insert
    void add(CheckItem checkItem);

    // select by value
    Page<CheckItem> selectByCondition(String queryString);

    // select Id count
    long findCountByCheckItemId(Integer id);

    // delete by Id
    void deleteById(Integer id);

    // find by Id return values
    CheckItem findById(Integer id);

    // update
    void edit(CheckItem checkItem);
}
