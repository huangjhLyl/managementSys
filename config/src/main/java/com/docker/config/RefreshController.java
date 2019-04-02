package com.docker.config;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * @author huangjh
 * @date 2019/4/2 12:30
 */
@Controller
public class RefreshController {
    @RequestMapping("/refresh/myRefresh")
    public void myRefresh(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        //设置content_type为json要不然会报415的错误
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,"application/json");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null,httpHeaders);
        //以post方法访问真正的刷新链接
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://localhost:8080/actuator/bus-refresh",
                request, String.class);
    }
}
