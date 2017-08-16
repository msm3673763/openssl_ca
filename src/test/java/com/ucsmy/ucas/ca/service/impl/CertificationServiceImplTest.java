package com.ucsmy.ucas.ca.service.impl;

import com.ucsmy.ucas.ca.service.CertificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Mongodb 测试类
 *
 * @author zhengfucheng
 * @version 1.0.0 2017-04-27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificationServiceImplTest {


    @Autowired
    private CertificationService certificationService;



    @Test
    public void testCreateCertificate() throws Exception {
        //byte[] bytes = certificationService.downloadCertificate("449bd1f2de53414c9488acb5802379ff", "ad0f099daf604ffd84ee27ebeee351d1");
        //System.out.println(bytes.length);
    }
}