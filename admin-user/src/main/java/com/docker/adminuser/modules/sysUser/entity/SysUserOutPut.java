package com.docker.adminuser.modules.sysUser.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@Data
public class SysUserOutPut implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 归属公司
     */
    private String companyId;

    /**
     * 归属部门
     */
    private String officeId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户角色
     */

    private String UserRoles;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    private LocalDateTime loginDate;

}
