package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.OrderSetting;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> data);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
