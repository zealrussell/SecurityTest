package com.zeal.securitytest.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * WHAT THE ZZZZEAL
 *
 * @Author zeal
 * @Date 2023/6/27 22:14
 * @Version 1.0
 */
@Data
@Getter
@Setter
public class Role {
    private Integer id;
    private String name;
    private String nameZh;
}
