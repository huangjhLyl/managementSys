package com.docker.feign.adminRole.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@Data
public class SysUserInPut implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 登录名
     */
    private String loginName;
}
