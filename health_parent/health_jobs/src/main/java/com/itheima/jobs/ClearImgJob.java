package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义Job,实现定时垃圾清理
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        // 计算两个集合的差值,返回一个含有全部差值的set集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null) {
            for (String picName : set) {
                QiniuUtils.deleteFileFromQiniu(picName);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
                System.out.println("clean done" + " " + picName);
            }
        }
    }
}
