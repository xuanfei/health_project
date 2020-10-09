package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckItemService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    // 使用JedisPool使用Redis服务
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;// 从属性文件中读取要生成的html对应的目录

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(checkgroupIds, setmealId);

        // 2. 当用户添加套餐后，将图片名称保存到redis的另一个Set集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);

        // 当添加完套餐后需要重新生成新的静态页面(套餐列表页、套餐详情页)
        this.generateMobileStaticHtml();
    }

    // 生成当前方法所需的静态页面
    public void generateMobileStaticHtml() {
        // 在生成静态页面之前需要查询数据
        List<Setmeal> allSetmeal = setmealDao.getAllSetmeal();
        // 需要生成套餐列表静态页面
        generateMobileSetmealListHtml(allSetmeal);
        // 套餐详情静态页面
        generateMobileSetmealDetailHtml(allSetmeal);
    }

    /**
     * 生成套餐列表静态页面方法
     *
     * @param list 套餐的getAll信息
     */
    public void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map<String, Object> map = new HashMap<>();
        map.put("setmealList", list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    /**
     * 生成套餐详情静态页面 (可能多个)
     */
    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map<String, Object> map = new HashMap<>();
            Setmeal setmeallist = setmealDao.findSetMealAndItemById(setmeal.getId());
            map.put("setmeal", setmeallist);
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_" + setmeal.getId() + ".html",map);
        }
    }

    /**
     * 通用, 用于生成静态页面方法
     *
     * @param teplateName  使用的模板名
     * @param htmlPageName 生成的静态页面名
     * @param map          替换参数对应map
     */
    public void generateHtml(String teplateName, String htmlPageName, Map map) {
        // 在spring配置文件中已经配好了相关的参数(字符集、模板所在目录等)
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 根据模板名获得一个模板对象,需要一个输出流输出这个静态页面
            Template template = configuration.getTemplate(teplateName);

            // 默认是用这个
//            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
//            template.process(map, out);

            // 输出地址、文件名等信息定义
            // 拼串得到输出的地址和文件名, 地址从prop中获取, 文件名通过参数传递
            out = new FileWriter(outPutPath + "/" + htmlPageName);
            String dir = outPutPath + "/" + htmlPageName;
            // 开始写出文件
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(dir), "utf-8");
            PrintWriter printWriter = new PrintWriter(writer);
            // 输出文件
            template.process(map, printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, originsetmeal.getImg());

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        setmealDao.deleteAssoication(setmeal.getId());
        this.setSetmealAndCheckGroup(checkgroupIds, setmeal.getId());
        setmealDao.edit(setmeal);
    }

    @Override
    public void delete(Integer id) {
        Setmeal setmeal = this.findById(id);
//        QiniuUtils.deleteFileFromQiniu(setmeal.getImg());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        setmealDao.deleteAssoication(id);
        setmealDao.deleteById(id);
    }

//    @Override
//    public Setmeal findSetMealAndItemById(int id) {
//        return setmealDao.findSetMealAndItemById(id);
//    }

    @Override
    public List<Setmeal> getAllSetmeal() {
        return setmealDao.getAllSetmeal();
    }

    @Override
    public Setmeal getSetmealsById(int id) {
        Setmeal setmeal = setmealDao.findById(id);
        List<Integer> checkGroupIds = setmealDao.findCheckGroupIdsBySetMealId(id);
        List<CheckGroup> checkGroups = new ArrayList<>();
        for (Integer checkGroupId : checkGroupIds) {
            CheckGroup checkGroup = checkGroupDao.findById(checkGroupId);
            List<Integer> checkitemIds = checkGroupDao.findCheckitemIdsByCheckGroupId(checkGroupId);
            List<CheckItem> checkItems = new ArrayList<>();
            for (Integer checkitemId : checkitemIds) {
                CheckItem checkItem = checkItemDao.findById(checkitemId);
                checkItems.add(checkItem);
                checkGroup.setCheckItems(checkItems);
            }
            checkGroups.add(checkGroup);
            setmeal.setCheckGroups(checkGroups);
        }
        return setmeal;
    }

    // 查询套餐预约占比
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
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
