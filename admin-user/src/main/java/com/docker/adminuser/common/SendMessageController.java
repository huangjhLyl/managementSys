package com.docker.adminuser.common;

import com.docker.adminuser.message.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author huangjh
 * @date 2019/4/3 15:47
 */
@RestController
public class SendMessageController {

    @Autowired
    private StreamClient streamClient;


    @GetMapping("/sendMessage")
    public void process(){
        String message = "now" + new Date();
        streamClient.outPut().send(MessageBuilder.withPayload(message).build());
    }
}
