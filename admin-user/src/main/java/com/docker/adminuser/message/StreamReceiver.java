package com.docker.adminuser.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author huangjh
 * @date 2019/4/3 15:30
 */
@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReceiver {

    @StreamListener("outPut")
    public void process(Object message){

        log.info("StreamReceiver,{}",message);
    }
}
