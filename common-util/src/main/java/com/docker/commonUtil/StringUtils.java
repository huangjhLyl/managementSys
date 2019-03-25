package com.docker.commonUtil;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author huangjh
 * @date 2019/3/8 14:15
 */
public class StringUtils {
    public static JSONObject stringToJson(String str) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj = (JSONObject)(new JSONParser().parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
