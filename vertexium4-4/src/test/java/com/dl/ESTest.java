package com.dl;

import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESTest {
    @Test
    public void transport() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("cluster.name","elasticsearch")
                .put("transport.tcp.connect_timeout", "120s")
                .put("xpack.security.user", "root:123456")
                .put("xpack.security.transport.ssl.enabled", "true")
                .put("xpack.security.transport.ssl.verification_mode", "certificate")
                .put("xpack.security.transport.ssl.keystore.path", "/Users/daile/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/661a65a6eef25e2dbdc0d031ae25074e/Message/MessageTemp/792276c40ccf59284e3ae67050a85d8a/OpenData/elastic-certificates.p12")//文件目录
                .put("xpack.security.transport.ssl.truststore.path", "/Users/daile/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/661a65a6eef25e2dbdc0d031ae25074e/Message/MessageTemp/792276c40ccf59284e3ae67050a85d8a/OpenData/elastic-certificates.p12")//文件目录
                .put("xpack.security.transport.ssl.truststore.password", "123456")
                .put("xpack.security.transport.ssl.keystore.password","123456")
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        TransportClient kerberos = client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("kerberos"), 9300));
        ClusterAdminClient cluster = kerberos.admin().cluster();
        System.out.println(1);

    }
}
