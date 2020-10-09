package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作类
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码——体检预约
     * @param telephone 电话号码
     * @return 结果集
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 生成4位的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 60*5, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 发送验证码——快速登陆
     * @param telephone 电话号码
     * @return 结果集
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //随机生成6位的数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            // 给用户发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        // 并保存验证码给redis临时存5分钟
        // 保存手机号码时保证唯一的方法: 电话 + 三位固定编号
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 60*5, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
