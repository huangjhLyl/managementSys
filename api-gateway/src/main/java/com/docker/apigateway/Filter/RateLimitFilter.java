//package com.docker.apigateway.Filter;
//
//import com.docker.apigateway.exception.RateLimitException;
//import com.google.common.util.concurrent.RateLimiter;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.exception.ZuulException;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
//
///**
// * @author huangjh
// * @date 2019/4/4 23:40
// */
//public class RateLimitFilter extends ZuulFilter {
//
//    private static RateLimiter RATE_LIMITER = RateLimiter.create(1000);
//    @Override
//    public String filterType() {
//        return PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return SERVLET_DETECTION_FILTER_ORDER  - 1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        if(!RATE_LIMITER.tryAcquire()){
//            throw new RateLimitException();
//        }
//        return null;
//    }
//}
