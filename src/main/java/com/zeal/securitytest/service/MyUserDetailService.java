package com.zeal.securitytest.service;

import com.zeal.securitytest.dao.UserDao;
import com.zeal.securitytest.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/27 21:17
 * @Version 1.0
 */
// 把用户名封装成userdetail信息返回
@Service
public class MyUserDetailService implements UserDetailsService, UserDetailsPasswordService {

    // 从dao中到 springboot
    private final UserDao userDao;
    @Autowired
    public MyUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.查询用户
        User user = userDao.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名不正确");
        // 2. 获取角色
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Integer integer = userDao.updatePassword(user.getUsername(), newPassword);
        if (integer == 1) {
            ((User)user).setPassword(newPassword);
        }
        return user;
    }
}
