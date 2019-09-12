package com.wxscistor.util;

import com.wxscistor.config.VertexiumConfig;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.admin.SecurityOperations;
import org.apache.accumulo.core.security.Authorizations;
import org.vertexium.accumulo.AccumuloAuthorizations;
import org.vertexium.accumulo.AccumuloGraph;

import java.util.Arrays;

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

    public static AccumuloAuthorizations getRootAuth(AccumuloGraph accumuloGraph) throws AccumuloSecurityException, AccumuloException {
        Authorizations root = accumuloGraph.getConnector().securityOperations().getUserAuthorizations("root");
        return new AccumuloAuthorizations(root.toString().split(","));
    }

    public static void addRootAuth(String[] addauths) throws AccumuloSecurityException, AccumuloException {
        String addAuth = "";
        for (String auth:addauths) {
            addAuth += auth;
        }
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        SecurityOperations securityOperations = graph.getConnector().securityOperations();
        String user = "root";
        Authorizations auths = null;
        try {
            auths = securityOperations.getUserAuthorizations(user);
            StringBuilder userAuths = new StringBuilder();
            if (!auths.isEmpty()) {
                userAuths.append(auths.toString());
                userAuths.append(",");
            }
            userAuths.append(addAuth);
            Authorizations newAuth = AuthUtils.parseAuthorizations(userAuths.toString());
            securityOperations.changeUserAuthorizations(user,newAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRootAuth(String addAuth) throws AccumuloSecurityException, AccumuloException {
        AccumuloGraph graph = VertexiumConfig.defaultGraph;
        SecurityOperations securityOperations = graph.getConnector().securityOperations();
        String user = "root";
        Authorizations auths = null;
        try {
            auths = securityOperations.getUserAuthorizations(user);
            StringBuilder userAuths = new StringBuilder();
            if (!auths.isEmpty()) {
                userAuths.append(auths.toString());
                userAuths.append(",");
            }
            userAuths.append(addAuth);
            Authorizations newAuth = AuthUtils.parseAuthorizations(userAuths.toString());
            securityOperations.changeUserAuthorizations(user,newAuth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
