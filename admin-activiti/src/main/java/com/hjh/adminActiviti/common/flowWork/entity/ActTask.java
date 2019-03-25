package com.hjh.adminActiviti.common.flowWork.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class ActTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String proIntId;
    /**
     * 流程定义文件ID
     */
    private String proDefId;
    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 流程版本
     */
    private int flowVersion;

    /**
     * 当前环节名称
     */
    private String currTaskName;
    /**
     * 上一环节名称
     */
    private String supTaskName;
    /**
     * 下一环节名称
     */
    private String nextTaskName;

    /**
     *当前办理人
     */
    private String assignName;
}
