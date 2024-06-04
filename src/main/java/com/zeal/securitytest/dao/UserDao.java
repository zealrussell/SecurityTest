package com.zeal.securitytest.dao;

import com.zeal.securitytest.entity.Role;
import com.zeal.securitytest.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/27 22:40
 * @Version 1.0
 */
@Mapper
public interface UserDao   {
    // 根据用户名查询用户
    User loadUserByUsername(String username);

    // 根据用户id查询角色
    List<Role> getRolesByUid(Integer uid);

    // 3. 根据用户名更新密码方法
    Integer updatePassword(@Param("username") String username, @Param("password") String password);
}
