package com.docker.adminuser;

import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 发送消息测试类
 * @author huangjh
 * @date 2019/4/2 17:55
 */
@Component
public class MySenderTest extends AdminUserApplicationTests{

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void send(){
        amqpTemplate.convertAndSend("myQueue","now "+new Date());
    }
}
