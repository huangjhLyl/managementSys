package com.hjh.adminActiviti.common.util;

import com.hjh.adminActiviti.common.flowWork.service.IActModelService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author huangjh
 * @date 2019/3/13 13:48
 */
@Component
public class XmlUtil {
    private final static Logger logger = LoggerFactory.getLogger("xmlUtil");

    @Autowired
    private IActModelService actModelService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;
    /**
     * 读取资源，通过部署ID
     * @param processDefinitionId  流程定义ID
     * @param processInstanceId 流程实例ID
     * @param resourceType 资源类型(xml|image)
     */
    public InputStream resourceRead(String procDefId, String proInsId, String resType) throws Exception {

        if (procDefId == null || "".equals(procDefId)){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId).singleResult();
            procDefId = processInstance.getProcessDefinitionId();
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();

        String resourceName = "";
        if (resType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }

        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        return resourceAsStream;
    }
}
