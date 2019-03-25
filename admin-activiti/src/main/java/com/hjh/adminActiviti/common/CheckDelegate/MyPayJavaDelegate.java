package com.hjh.adminActiviti.common.CheckDelegate;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * test
 *
 * @author jimmy
 **/
public class MyPayJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPayJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("variables = {}",execution.getVariables());
        LOGGER.info("run my java delegate {}", this);
        Object money = execution.getVariable("money");
        int checkMoney = 0;
        if(money instanceof  String){
            checkMoney = Integer.parseInt((String)money);
        }
        int deptMoney = 1000000;
        LOGGER.info("部门经费 ={}",deptMoney);
        LOGGER.info("提交金额 ={}",checkMoney);
        if (checkMoney>deptMoney) {
            LOGGER.info("发生异常");
            throw new BpmnError("bpmnError");
        }

    }
}
