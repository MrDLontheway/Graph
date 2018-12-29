package com.wxscistor.config;

import com.wxscistor.util.AuthUtils;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.client.admin.*;
import org.apache.accumulo.core.security.Authorizations;
import java.io.InputStream;
import java.util.Properties;

public class AccumuloConnector {
    public static String instanceName;
    public static String zooServers;
    public static Connector accumuloConn;
    public static Instance inst;
    public static String user = "root";
    public static String pwd;
    public static Properties properties = new Properties();

    static {
        try {
            InputStream in = VertexiumConfig.class.getClassLoader().getResourceAsStream("vertexiumConf.properties");
            properties.load(in);
            instanceName = properties.getProperty("accumuloInstanceName","accumulo");
            zooServers = properties.getProperty("zookeeperServers","");
            user = properties.getProperty("username","root");
            pwd = properties.getProperty("password","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        AccumuloConnector.instanceName = instanceName;
    }

    public static String getZooServers() {
        return zooServers;
    }

    public void setZooServers(String zooServers) {
        AccumuloConnector.zooServers = zooServers;
    }

    public static Connector getAccumuloConn() {
        return accumuloConn;
    }

    public static void setAccumuloConn(Connector accumuloConn) {
        AccumuloConnector.accumuloConn = accumuloConn;
    }

    public static Instance getInst() {
        return inst;
    }

    public static void setInst(Instance inst) {
        AccumuloConnector.inst = inst;
    }

    public static String getUser() {
        return user;
    }

//    @Value("${username}")
    public void setUser(String user) {
        AccumuloConnector.user = user;
    }

    public static String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        AccumuloConnector.pwd = pwd;
    }

    public static void initAccumulo(){
        inst = new ZooKeeperInstance(instanceName, zooServers);
        try {
            AccumuloConnector.accumuloConn = inst.getConnector(user, pwd);
            VertexiumConfig.rootAuth = accumuloConn.securityOperations().getUserAuthorizations(user).toString();
            System.out.println("连接accumulo!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接异常accumulo!");
        }
    }

    public static synchronized String addAuths(String user,String addAuth) throws Exception {
        SecurityOperations securityOperations = accumuloConn.securityOperations();
        String old = securityOperations.getUserAuthorizations("root").toString();
        String[] split = old.split(",");
        for (String s:
        split) {
            if(s.equals(addAuth))
                return old;
        }
        String newAuth = securityOperations.getUserAuthorizations(user).toString() + "," + addAuth;
        String[] strings = AuthUtils.splitString(",", newAuth);
        //权限控制器
        securityOperations.changeUserAuthorizations(user,new Authorizations(strings));
        VertexiumConfig.rootAuth = newAuth;
        return newAuth;
    }

    public static Authorizations getRootAuths() throws Exception {
        SecurityOperations securityOperations = accumuloConn.securityOperations();
        return securityOperations.getUserAuthorizations("root");
    }

    public SecurityOperations getSecurityOperations(){
        return accumuloConn.securityOperations();
    }

    public InstanceOperations getInstanceOperations(){
        return accumuloConn.instanceOperations();
    }

    public NamespaceOperations getNamespaceOperations(){
        return accumuloConn.namespaceOperations();
    }

    public ReplicationOperations getReplicationOperations(){
        return accumuloConn.replicationOperations();
    }

    public TableOperations getTableOperations(){
        return accumuloConn.tableOperations();
    }

    public Connector getConnector(){
        return accumuloConn;
    }
}
