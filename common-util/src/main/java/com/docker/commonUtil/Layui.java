package com.docker.commonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author huangjh
 * @date 2019/3/6 15:12
 */
public class Layui  extends HashMap<String, Object> {

    public static Layui data(Integer count, List<?> data){
        Layui r = new Layui();
        r.put("code", 0);
        r.put("msg", "");
        r.put("count", count);
        r.put("data", data);
        return r;
    }
}

