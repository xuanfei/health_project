package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBea);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetMealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id);

//    Setmeal findSetMealAndItemById(int id);

    List<Setmeal> getAllSetmeal();

    Setmeal getSetmealsById(int id);

    List<Map<String,Object>> findSetmealCount();
}
