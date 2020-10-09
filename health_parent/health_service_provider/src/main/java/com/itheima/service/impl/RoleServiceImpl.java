package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.service.CheckGroupService;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<Role> page = roleDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        roleDao.deleteAssoication(id);
        roleDao.deleteAssoicationOfMenu(id);
        roleDao.deleteByRoleId(id);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.add(role);
        Integer roleId = role.getId();
        setRoleAndPermission(permissionIds, roleId);
        setRoleAndMenu(menuIds, roleId);
    }

    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        Integer roleId = role.getId();
        roleDao.deleteAssoication(roleId);
        roleDao.deleteAssoicationOfMenu(roleId);
        setRoleAndPermission(permissionIds, roleId);
        setRoleAndMenu(menuIds, roleId);
        roleDao.edit(role);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        return roleDao.findMenuIdsByRoleId(id);
    }

    /**
     * 建立项与组的关系
     */
    private void setRoleAndPermission(Integer[] permissionIds, Integer roleId) {
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("roleId", roleId);
                map.put("permissionId", permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }

    /**
     * 建立角色与菜单显示的关系
     */
    private void setRoleAndMenu(Integer[] menuIds, Integer roleId) {
        if (menuIds != null && menuIds.length > 0) {
            for (Integer menuId : menuIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("roleId", roleId);
                map.put("menuId", menuId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }
}
