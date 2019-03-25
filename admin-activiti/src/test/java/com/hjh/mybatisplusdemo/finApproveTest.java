package com.hjh.adminActiviti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author huangjh
 * @date 2019/2/27 15:41
 * 费用报销流程
 */
public class finApproveTest{
    private final Logger logger = LoggerFactory.getLogger(finApproveTest.class);


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 测试审批
     */
    @Test
    public void testApprove(){
        //部署流程
        Deployment deploy = getDeployment();

        //获取流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(deploy);

        //启动流程
        ProcessInstance processInstance = getProcessInstance(processDefinition);

        //查询任务
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .listPage(0, 100);
        logger.info("报销审批流程任务数={}",tasks.size());
        for (Task task : tasks) {
            logger.info("报销审批流程任务={}",task.getName());
        }
    }

    private ProcessInstance getProcessInstance(ProcessDefinition processDefinition) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        logger.info("启动流程成功 流程实例id={}",processInstance.getProcessDefinitionId());
        return processInstance;
    }

    private ProcessDefinition getProcessDefinition(Deployment deploy) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        logger.info("报销审批流程定义文件 ={}",processDefinition);
        return processDefinition;
    }

    private Deployment getDeployment() {
        Deployment deploy = repositoryService.createDeployment()
                .name("报销审批流程")
                .addClasspathResource("processes/acc-approve.bpmn20.xml")
                .deploy();
        logger.info("报销审批流程部署文件  =[{}],部署名称为 [{}]",deploy,deploy.getName());
        return deploy;
    }
}
