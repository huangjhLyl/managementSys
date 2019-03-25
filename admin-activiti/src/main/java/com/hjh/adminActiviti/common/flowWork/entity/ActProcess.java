package com.hjh.adminActiviti.common.flowWork.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangjh
 * @date 2019/3/8 11:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActProcess implements Serializable{
    /**
     * 流程ID
     */
    private String id;
    /**
     * 流程标识
     */
    private String key;
    /**
     * 流程名称
     */
    private  String name;
    /**
     * 流程版本
     */
    private int version;
    /**
     * 部署时间
     */
    private Date deploymentTime;
    /**
     * 流程xml
     */
    private String resourceName;
    /**
     * 流程图片
     */
    private String diagramResourceName;

}
