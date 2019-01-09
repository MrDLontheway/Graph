package com.wxscistor.util;

import org.apache.accumulo.core.security.Authorizations;
import org.vertexium.accumulo.AccumuloAuthorizations;

import java.util.Arrays;
import java.util.List;

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

    public static AccumuloAuthorizations getUserAuth(String[] dsr,String user){
        String[] newAuth = Arrays.copyOf(dsr, dsr.length + 1);
        newAuth[newAuth.length-1] = user;
        return new AccumuloAuthorizations(newAuth);
    }

    public static String authsToString(String[] auths){
        String result = "";
        for (int i = 0; i < auths.length; i++) {
            if(i==auths.length-1){
                result += auths[i];
            }else {
                result += auths[i]+",";
            }
        }
        return result;
    }

    public static String authsToString(String[] auths,String user){
        String result = "";
        for (int i = 0; i < auths.length; i++) {
            result += auths[i]+",";
        }
        result += user;
        return result;
    }

    public static Authorizations parseAuthorizations(final String field) {
        if (field == null || field.isEmpty()) {
            return Authorizations.EMPTY;
        }
        return new Authorizations(field.split(","));
    }
}
