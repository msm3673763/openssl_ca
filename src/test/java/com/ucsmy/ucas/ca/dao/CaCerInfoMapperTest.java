package com.ucsmy.ucas.ca.dao;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/3

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.ucas.ca.ext.CertResponseBody;
import com.ucsmy.ucas.ca.ext.CertificationPojo;
import com.ucsmy.ucas.ca.utils.TwaGateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * CaCerInfoMapper测试类
 *
 * @author ucs_masiming
 * @since 2017/8/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CaCerInfoMapperTest {

    @Autowired
    private CaCerInfoMapper caCerInfoMapper;
    @Autowired
    private CertificationMapper certificationMapper;

    @Test
    public void testQueryCACertification() {
        CertificationPojo pojo = certificationMapper.queryCACertification();
        System.out.println(pojo.getFileType());
    }

    @Test
    public void testGetBeanById() {
        CertResponseBody body = caCerInfoMapper.getBeanById("100017080315000006");
        System.out.println(body.getP12Secret());
    }

    @Test
    public void testTwaGate() {
        try {
            Map<String, Object> map = TwaGateUtil.getInstance().httpGet("https://172.17.21.59:9443/twa-gate/certification/refreshCrl");
            System.out.println(map.get("retcode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
