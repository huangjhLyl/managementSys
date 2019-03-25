package com.docker.adminRole.modules.sysRole.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author huangjh
 * @since 2019-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 归属机构
     */
    private String officeId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String enname;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 数据范围
     */
    private String dataScope;

    /**
     * 是否系统数据
     */
    private String isSys;

    /**
     * 是否可用
     */
    private String useable;

    /**
     * 备注信息
     */
    private String remarks;


}
