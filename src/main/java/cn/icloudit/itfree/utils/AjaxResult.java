package cn.icloudit.itfree.utils;

import java.util.HashMap;
import java.util.Map;

/****
 *
 * ajax结果返回
 */
public class AjaxResult {

    public static Object error(String msg){
        Map<String, Object> map = new HashMap<>();
        map.put("code", "500");
        map.put("msg", msg);
        return map;
    }

    public static Object success(String msg){
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        map.put("msg", msg);
        return map;
    }
}
