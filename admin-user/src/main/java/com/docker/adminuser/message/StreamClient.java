package com.docker.adminuser.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author huangjh
 * @date 2019/4/3 15:27
 */
public interface StreamClient {

    @Input("input")
    SubscribableChannel input();

    @Output("outPut")
    MessageChannel outPut();
}
