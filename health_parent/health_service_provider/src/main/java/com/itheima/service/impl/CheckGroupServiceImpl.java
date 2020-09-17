package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    // 新增检查组，同时让检查组关联检查项，需要分两步
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 新增检查组，操作t_checkgroup表
        checkGroupDao.add(checkGroup);
        // 重新建立关联关系
        Integer checkGroupId = checkGroup.getId();
        Reconnection(checkGroupId, checkitemIds);
    }

    // 分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckitemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckitemIdsByCheckGroupId(id);

    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 修改检查组基本基本信息，操作t_checkgroup表
        checkGroupDao.edit(checkGroup);
        // 清理当前检查组关联的检查项，操作中间关系表t_checkgroup_checkitem
        checkGroupDao.deleteAssoication(checkGroup.getId());
        // 重新建立关联关系
        Integer checkGroupId = checkGroup.getId();
        Reconnection(checkGroupId, checkitemIds);
    }

    @Override
    public void delete(Integer checkGroupId) {
        try {
            checkGroupDao.deleteAssoication(checkGroupId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        checkGroupDao.deleteByGroupId(checkGroupId);
    }

    /**
     * 重新建立检查组和检查项的关系
     * @param checkGroupId 检查组Id
     * @param checkitemIds 检查项Id
     */
    private void Reconnection(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            // 设置检查组和检查项的关联关系(多对多)t_checkgroup_checkitem
            for (Integer checkitemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkGroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
