package com.hjh.adminActiviti.common.util.ActivitiDraw;

/**
 * @author huangjh
 * @date 2019/3/12 17:28
 */
/**
 * 节点和连线的父类.
 */
public class GraphElement {
    /**
     * 实例id，历史的id.
     */
    private String id;

    /**
     * 节点名称，bpmn图形中的id.
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
