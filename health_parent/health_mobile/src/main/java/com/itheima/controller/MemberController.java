package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RequestMapping("/member")
@RestController
public class MemberController {

    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        // 取出手机号
        String telephone = (String) map.get("telephone");
        // 取出输入的验证码
        String validateCode = (String) map.get("validateCode");
        // 生成的验证码临时存入redis
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        // 两方验证码比对，如果比对成功：
        if (validateCodeInRedis != null && validateCode != null && validateCodeInRedis.equals(validateCode)) {
            // 判断当前用户是否为会员，查会员表
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                member = new Member();// 初始化
                // 不是会员，自动完成注册
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            // 向客户端浏览器写入Cookie
            Cookie cookie = new Cookie("member_telephone",telephone);
            cookie.setPath("/"); // 路径
            cookie.setMaxAge(60*60*24*30);// 生存时间
            response.addCookie(cookie);// Cookie装填
            // 保存member信息保存redis
            // redis无法直接保存对象, 首先要把对象变成Json字符串
            String json = JSON.toJSON(member).toString();
            // cookie存入 存活时间30min
            jedisPool.getResource().setex(telephone, 60*30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            // 验证码比对失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
