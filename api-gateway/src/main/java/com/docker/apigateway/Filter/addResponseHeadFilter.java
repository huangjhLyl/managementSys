//package com.docker.apigateway.Filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import java.util.UUID;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;
//
///**
// * @author huangjh
// * @date 2019/4/4 23:00
// */
//@Component
//public class addResponseHeadFilter extends ZuulFilter {
//    @Override
//    public String filterType() {
//        return POST_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return SEND_RESPONSE_FILTER_ORDER -1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        HttpServletResponse response = currentContext.getResponse();
//        response.setHeader("X-foo", UUID.randomUUID().toString());
//        return null;
//    }
//}
