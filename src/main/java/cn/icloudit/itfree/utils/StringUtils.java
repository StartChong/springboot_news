package cn.icloudit.itfree.utils;

import java.util.UUID;


/****
 *
 * 自定义字符串判断
 */
public class StringUtils {

    public static boolean isNotEmpty(Object obj){
        if (obj != null && !"".equals(obj)){
            return true;
        } else {
            return false;
        }
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
