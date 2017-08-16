package com.ucsmy.ucas.ca.utils;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/10

 * Contributors:
 *      - initial implementation
 */

import com.squareup.okhttp.*;
import com.ucsmy.ucas.ca.Exception.CertException;
import com.ucsmy.ucas.ca.constant.ConfigConstant;
import com.ucsmy.ucas.manage.dao.ManageConfigMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/8/10
 */
@Component
public class TwaGateUtil implements ApplicationContextAware {

    public volatile static TwaGateUtil instance;
    public static OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    private ManageConfigMapper manageConfigMapper;

    public static synchronized TwaGateUtil getInstance() {
        return instance;
    }

    public void init() throws Exception {
        String trustStorePath = manageConfigMapper.queryValueByKey(ConfigConstant.TRUST_STORE_PATH);
        ValidationAssert.notEmpty(trustStorePath, "参数表没配置信任库地址");
        String trustStorePass = manageConfigMapper.queryValueByKey(ConfigConstant.TRUST_STORE_PASS);
        ValidationAssert.notEmpty(trustStorePath, "参数表没配置信任库密码");
        String keyStorePath = manageConfigMapper.queryValueByKey(ConfigConstant.KEY_STORE_PATH);
        ValidationAssert.notEmpty(trustStorePath, "参数表没配置密钥库地址");
        String keyStorePass = manageConfigMapper.queryValueByKey(ConfigConstant.KEY_STORE_PASS);
        ValidationAssert.notEmpty(trustStorePath, "参数表没配置密钥库密码");

        //初始化truststore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(trustStorePath), trustStorePass.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        //初始化keystore
        KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
        clientKeyStore.load(new FileInputStream(new File(keyStorePath)), keyStorePass.toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, keyStorePass.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        client = new OkHttpClient();
        client.setSslSocketFactory(sslContext.getSocketFactory());
		client.setHostnameVerifier(instance.new NullHostNameVerifier());//忽略hostname的验证
    }

    public Map<String, Object> httpGet(String url) throws Exception {
        if (client == null) {
            init();
        }
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new CertException("刷新双向网关吊销列表失败！connect fail");
        }
        return JsonUtils.strJson2Map(response.body().string());
    }

    public Map<String, Object> httpPostForJson(String url, String jsonStr) throws Exception {
        if (client == null) {
            init();
        }
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url(url).post(body).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new CertException("Unexpected code " + response);
        }
        return JsonUtils.strJson2Map(response.body().string());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance = (TwaGateUtil) applicationContext.getBean("twaGateUtil");
    }

    public class NullHostNameVerifier implements HostnameVerifier {
        /*
         * (non-Javadoc)
         *
         * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
         * javax.net.ssl.SSLSession)
         */
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }
}
