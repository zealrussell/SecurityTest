package com.zeal.securitytest.entity;

import lombok.*;
import org.apache.el.parser.BooleanNode;
import org.apache.ibatis.javassist.Loader;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/27 22:14
 * @Version 1.0
 */
// 自定义用户
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Boolean enabled;    //  用户是否激活
    private Boolean accountNonExpired;  //账户是否过期
    private Boolean accountNonLocked;   //账户是否被锁定
    private Boolean credentialsNonExpired;  //密码是否过期
    private List<Role> roles = new ArrayList<>();   // 关系属性 存储当前用户所有角色
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(simpleGrantedAuthority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
