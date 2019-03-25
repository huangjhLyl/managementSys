package com.hjh.adminActiviti.common.flowWork.controller;

import com.docker.commonUtil.Layui;
import com.google.common.collect.Lists;
import com.hjh.adminActiviti.common.flowWork.entity.ActProcess;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

/**
 * 流程定义
 * @author huangjh
 * @date 2019/3/8 10:57
 */
@Controller
@RequestMapping("/activiti/act-process")
public class ActProcessController {
    @Autowired
    private RepositoryService repositoryService;
    /**
     * 流程定义列表
     */
    @RequestMapping("list")
    @ResponseBody
    public Layui processList(int page, int limit) {

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .active()
                .orderByProcessDefinitionKey().asc();


        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(0, 100);

        LinkedList<ActProcess> actProcessesList = Lists.newLinkedList();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            ActProcess actProcess = new ActProcess();//流程定义文件封装对象，一个是流程定义文件，一个是流程部署对象
            actProcess.setId(processDefinition.getId());//流程定义ID
            actProcess.setKey(processDefinition.getKey());//流程定义key
            actProcess.setName(processDefinition.getName());//流程定义名称
            actProcess.setVersion(processDefinition.getVersion());//流程版本
            actProcess.setResourceName(processDefinition.getResourceName());//流程xml
            actProcess.setDiagramResourceName(processDefinition.getDiagramResourceName());//流程顶替图片

            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

            actProcess.setDeploymentTime(deployment.getDeploymentTime());//流程部署时间

            actProcessesList.add(actProcess);
        }

        return Layui.data(actProcessesList.size(),actProcessesList);
    }

    /**
     * 流程定义列表（空）
     */
    @RequestMapping("listEmpity")
    public String processListEmp() {

        return "flowWork/listActProcess";
    }
}
