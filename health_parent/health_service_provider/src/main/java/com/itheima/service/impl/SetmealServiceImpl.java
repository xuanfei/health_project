package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    // 使用JedisPool使用Redis服务
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(checkgroupIds, setmealId);

        // 2. 当用户添加套餐后，将图片名称保存到redis的另一个Set集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBea) {
        Integer currentPage = queryPageBea.getCurrentPage();
        Integer pageSize = queryPageBea.getPageSize();
        String queryString = queryPageBea.getQueryString();
        PageHelper.startPage(currentPage, pageSize);

        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetMealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetMealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        Setmeal originsetmeal = this.findById(setmeal.getId());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,originsetmeal.getImg());

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        setmealDao.deleteAssoication(setmeal.getId());
        this.setSetmealAndCheckGroup(checkgroupIds, setmeal.getId());
        setmealDao.edit(setmeal);
    }

    @Override
    public void delete(Integer id) {
        Setmeal setmeal = this.findById(id);
//        QiniuUtils.deleteFileFromQiniu(setmeal.getImg());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        setmealDao.deleteAssoication(id);
        setmealDao.deleteById(id);
    }

    /**
     * 建立套餐与组的关系
     */
    private void setSetmealAndCheckGroup(Integer[] checkgroupIds, Integer setmealId) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
