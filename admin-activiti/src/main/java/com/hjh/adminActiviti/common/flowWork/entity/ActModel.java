package com.hjh.adminActiviti.common.flowWork.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  流程设计表
 * </p>
 *
 * @author huangjh
 * @since 2019-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程文件id
     *
     */
    @TableField("ID_")
    private String ID;

    /**
     * 流程部署文件名字
     */
    @TableId("NAME_")
    private String name;

    /**
     * 流程部署文件KEY
     */
    @TableField("KEY_")
    private String key;

    /**
     * 流程类型
     */
    @TableField("TYPE_")
    private String type;

    /**
     * 版本号
     */
    private int version;
    /**
     * 状态
     */
    @TableField("STATUS_")
    private String status;
}
