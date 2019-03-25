package com.hjh.adminActiviti.common.flowWork.controller;


import com.docker.commonUtil.ImageUtil;
import com.docker.commonUtil.Layui;
import com.google.common.collect.Lists;
import com.hjh.adminActiviti.common.flowWork.entity.ActTask;
import com.hjh.adminActiviti.common.util.ActivitiDraw.HistoryProcessInstanceDiagramCmd;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/**
 * <p>
 * 流程节点任务
 * </p>
 *
 * @author huangjh
 * @since 2019-03-06
 */
@Controller
@RequestMapping("/activiti/act-Task")
public class ActTaskController {
    private static  final Logger logger = LoggerFactory.getLogger(ActTaskController.class);

    private Boolean isPall = false;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Autowired
    private ManagementService workspaceService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @RequestMapping(value = "dealFLow",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String dealFLow(HttpServletRequest request, HttpServletResponse response){

        try {

            String type = request.getParameter("type");

            String taskId = request.getParameter("taskId");

            dealWork(type,taskId);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "流程处理成功";
    }

    @RequestMapping("taskToList")
    @ResponseBody
    public Layui taskToList(){
        List<ActTask> taskList = Lists.newLinkedList();
        List<Task> tasks = taskService.createTaskQuery()
                .orderByTaskId()
                .desc()
                .listPage(0, 100);
        try {
            for (Task task : tasks) {
                //设置任务参数值
                addTaskInfo(taskList, task);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return Layui.data(taskList.size(),taskList);
    }

    @RequestMapping("listEmpty")
    public String listEmpty(){
        return "flowWork/listToTask";
    }

    private void addTaskInfo(List<ActTask> taskList, Task task) throws Exception {
        ActTask actTask = new ActTask();
        actTask.setCurrTaskName(task.getName());//当前任务环节
        actTask.setProIntId(task.getProcessInstanceId());//当前流程实例id
        actTask.setTaskId(task.getId());//当前任务id
        actTask.setAssignName(task.getAssignee());//签收人或办理人
        actTask.setProDefId(task.getProcessDefinitionId());//流程定义文件ID
        //根据流程实例ID查询实例
        getProcess(task, actTask);
        //查询历史
        getHis(task, actTask);

        taskList.add(actTask);
    }

    private void getHis(Task task, ActTask actTask) {
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceEndTime()
                .desc()
                .listPage(0, 100);

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            if(!task.getName().equals(historicActivityInstance.getActivityName())
                    && !"exclusiveGateway".equals(historicActivityInstance.getActivityType())){
                actTask.setSupTaskName(historicActivityInstance.getActivityName());
                break;
            }
            logger.info("historicActivityInstance = {}",historicActivityInstance);
        }
    }

    private void getProcess(Task task, ActTask actTask) throws Exception {
        ProcessDefinition processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();
        if(processDefinitions != null && !"".equals(processDefinitions)){
            actTask.setFlowName(processDefinitions.getName());
            actTask.setFlowVersion(processDefinitions.getVersion());
            TaskDefinition nextTaskInfo = getNextTaskInfo(task.getProcessInstanceId());
            if(nextTaskInfo != null &&  !"".equals(nextTaskInfo)){
                actTask.setNextTaskName(nextTaskInfo.getNameExpression().getExpressionText());
            }
        }
    }

    /**
     * 获取下一个用户任务信息
     * @param String taskId     任务Id信息
     * @return  下一个用户任务用户组信息
     * @throws Exception
     */
    public TaskDefinition getNextTaskInfo(String processInstanceId) throws Exception {

        ProcessDefinitionEntity processDefinitionEntity = null;

        String id = null;

        TaskDefinition task = null;

        //获取流程实例Id信息
        //String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();

        //获取流程发布Id信息
        String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();

        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definitionId);

        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //当前流程节点Id信息
        String activitiId = execution.getActivityId();

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

        //遍历所有节点信息
        for(ActivityImpl activityImpl : activitiList){
            id = activityImpl.getId();
            if (activitiId.equals(id)) {
                //获取下一个节点信息
                task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
                break;
            }
        }
        return task;
    }

    /**
     * 下一个任务节点信息,
     *
     * 如果下一个节点为用户任务则直接返回,
     *
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务
     * @param ActivityImpl activityImpl     流程节点信息
     * @param String activityId             当前流程节点Id信息
     * @param String elString               排他网关顺序流线段判断条件
     * @param String processInstanceId      流程实例Id信息
     * @return
     */
    private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId){

        PvmActivity ac = null;

        Object s = null;

        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if (("userTask".equals(activityImpl.getProperty("type"))) && !activityId.equals(activityImpl.getId())) {
            // 获取该节点下一个节点信息
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
                    .getTaskDefinition();
            return taskDefinition;
        } else if("exclusiveGateway".equals(activityImpl.getProperty("type"))){// 当前节点为exclusiveGateway
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            //outTransitionsTemp = ac.getOutgoingTransitions();

            // 如果网关路线判断条件为空信息
          if (StringUtils.isEmpty(elString)) {
            // 获取流程启动时设置的网关判断条件信息
            elString = getGatewayCondition(activityImpl.getId(), processInstanceId);
          }
            // 如果排他网关只有一条线路信息
            if (outTransitions.size() == 1) {
                return nextTaskDefinition((ActivityImpl) outTransitions.get(0).getDestination(), activityId,
                        elString, processInstanceId);
            } else if (outTransitions.size() > 1) { // 如果排他网关有多条线路信息
                for (PvmTransition tr1 : outTransitions) {
                    s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
                    // 判断el表达式是否成立
                    if (isCondition(activityImpl.getId(), StringUtils.trim(s.toString()), elString)) {
                        return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
                                processInstanceId);
                    }

                }
            }
        }else if("parallelGateway".equals(activityImpl.getProperty("type"))){
            isPall = true;
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            for (PvmTransition outTransition : outTransitions) {
                return nextTaskDefinition((ActivityImpl) outTransition.getDestination(), activityId, elString,
                        processInstanceId);
            }
        }else {
            // 获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                ac = tr.getDestination(); // 获取线路的终点节点
                // 如果流向线路为排他网关
                if ("exclusiveGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();

                    // 如果网关路线判断条件为空信息
                    if (StringUtils.isEmpty(elString)) {
                        // 获取流程启动时设置的网关判断条件信息
                        elString = getGatewayCondition(ac.getId(), processInstanceId);
                    }

                    // 如果排他网关只有一条线路信息
                    if (outTransitionsTemp.size() == 1) {
                        return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
                                elString, processInstanceId);
                    } else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
                            // 判断el表达式是否成立
                            if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
                                return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
                                        processInstanceId);
                            }
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                } else {
                }
            }
            return null;
        }
        return null;
    }

    /**
     * 查询流程启动时设置排他网关判断条件信息
     * @param String gatewayId          排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
     * @param String processInstanceId  流程实例Id信息
     * @return
     */
    public String getGatewayCondition(String gatewayId, String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        Object object= runtimeService.getVariable(execution.getId(), gatewayId);
        return object==null? "":object.toString();
    }

    /**
     * 根据key和value判断el表达式是否通过信息
     * @param String key    el表达式key信息
     * @param String el     el表达式信息
     * @param String value  el表达式传入值信息
     * @return
     */
    public boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }

    private void dealWork(String type,String taskId) {
        Task nextTask = taskService.createTaskQuery().taskId(taskId).singleResult();

        logger.info("当前节点任务 ={}", nextTask.getName());

        String node = nextTask.getName();
        String userOwer = "";
        Map<String, Object> vars = new HashMap<String, Object>();
        if("上级审批".equals(node)){
            userOwer ="huangjh";
            Map<String, Object> variables = runtimeService.getVariables(nextTask.getProcessInstanceId());

            String money = (String) variables.get("money");
            vars.put("money",money);
        }else if("主办会计".equals(node)){
            userOwer ="huangjh";
        }else if("异常结束".equals(node) || "正常结束".equals(node)) {
            taskService.complete(nextTask.getId());
        }else if("财务主管".equals(node)){
            userOwer ="hjh";
        }else if("审批完成".equals(node)){
            taskService.complete(nextTask.getId());
        }else if("财务审批".equals(node)){
            userOwer ="huangjh";
        }else if("主办会计".equals(node)){
            userOwer = "huangjh";
        }else if("财务主管".equals(node)){
            userOwer = "hjh";
        }else{
            userOwer ="cap1";
        }
        if("0".equals(type)){
            //签收任务
            assignTask(userOwer,taskId);
        }else{
            //完成任务
            completeTask(nextTask, userOwer,taskId, vars);
        }
    }

    private void completeTask(Task nextTask, String userOwer,String taskId, Map<String, Object> vars) {
        List<Task> assignTask = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(userOwer)
                .listPage(0, 100);
        for (Task task : assignTask) {
            logger.info("任务提交 ={}",task.getName());
            taskService.complete(task.getId(),vars);
            logger.info("办理任务节点 ={}", nextTask.getName());
        }
    }

    private void assignTask(String userOwer,String taskId) {
        List<Task> unAssignTasks = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(userOwer)
                .taskUnassigned()
                .listPage(0, 100);
        for (Task unAssignTask : unAssignTasks) {
            logger.info("任务签收 ={}",unAssignTask.getName());
            try {
                taskService.claim(unAssignTask.getId(),userOwer);
            } catch (Exception e) {
                logger.info(e.getMessage(),e);
            }
        }
    }

    /**
     * <功能简述>流程跟踪图
     * <功能详细描述>
     * @param processInstanceId 流程实例id
     * @param response
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("graphHistoryProcessInstance")
    public void graphHistoryProcessInstance(HttpServletRequest request,HttpServletResponse response) throws Exception {

        String  processInstanceId = request.getParameter("processInstanceId");

        Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                processInstanceId);

        InputStream is = workspaceService.executeCommand(
                cmd);
        BufferedImage image = ImageIO.read(is);
        OutputStream out = response.getOutputStream();

        // 获取Graphics2D
        ImageUtil.setBackgroundColor(image, out);

        out.close();
        is.close();
    }




    /**
     * 读取带跟踪的图片
     */
    @RequestMapping(value = "/viewImage")
    public void viewImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 查询历史表中的Task
        String  processInstanceId = request.getParameter("processInstanceId");

        //获取历史流程实例
        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();
        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity,highLightedActivitList);

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }

        //中文显示的是口口口，设置字体就好了
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png"
                , highLightedActivitis,highLightedFlows,"宋体","宋体","宋体",null,1.0);
        //单独返回流程图，不高亮显示
//        InputStream imageStream = diagramGenerator.generatePngDiagram(bpmnModel);
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 获取需要高亮的线
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    private List<String> getHighLightedFlows(
            ProcessDefinitionEntity processDefinitionEntity,
            List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i)
                            .getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1)
                            .getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances
                        .get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances
                        .get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(
                        activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
                            .findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl
                    .getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
                        .getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }

}
