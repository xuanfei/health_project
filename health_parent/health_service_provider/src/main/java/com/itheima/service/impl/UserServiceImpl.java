package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 查询用户相关信息
 */
@Transactional
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    // 根据用户名查询数据库和关联的角色信息，同时还有角色的权限信息
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        Set<Role> roles = roleDao.findByUserId(user.getId());
        for (Role role : roles) {
            Set<Permission> permissions = permissionDao.findByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage, pageSize);
        Page<User> page = userDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(User user, Integer[] roleIds) {
        userDao.add(user);
        Integer userId = user.getId();
        setUserAndRole(roleIds, userId);
    }

    @Override
    public void delete(Integer id) {
        userDao.deleteAssoication(id);
        userDao.deleteByUserId(id);
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userDao.findRoleIdsByUserId(id);
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        userDao.deleteAssoication(user.getId());
        setUserAndRole(roleIds, user.getId());
        userDao.edit(user);
    }

    /**
     * 建立项与组的关系
     */
    private void setUserAndRole(Integer[] roleIds, Integer userId) {
        if (roleIds != null && roleIds.length > 0) {
            for (Integer checkitemId : roleIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("userId", userId);
                map.put("roleIds", checkitemId);
                userDao.setUserAndRole(map);
            }
        }
    }
}
