//package com.docker.adminuser.common;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author huangjh
// * @date 2019/4/2 8:54
// */
//
//@RefreshScope
//@RestController
//public class testMain {
//
//    @Value("${evn}")
//    private String evn;
//
//    @GetMapping("print")
//    public String print() {
//        return evn;
//    }
//}
