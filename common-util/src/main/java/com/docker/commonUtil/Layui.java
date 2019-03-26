package com.docker.commonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * @author huangjh
 * @date 2019/3/6 15:12
 */
public class Layui  extends HashMap<String, Object> {

    /**
     * 成功返回
     * @param count
     * @param data
     * @return
     */
    public static Layui data(Integer count, Object data){
        Layui r = new Layui();
        r.put("code", 0);
        r.put("msg", "成功");
        r.put("count", count);
        r.put("data", data);
        return r;
    }
}

