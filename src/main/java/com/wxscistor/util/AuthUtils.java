package com.wxscistor.util;

/**
 * @Author: dl
 * @Date: 2018/12/28 11:02
 * @Version 1.0
 */
public class AuthUtils {
    public static String[] splitString(String auth,String split){
        if("".equals(auth)||"".equals(split))
            return new String[0];
        return auth.split(split);
    }
}
