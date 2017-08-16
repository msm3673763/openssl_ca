package com.ucsmy.ucas.ca.controller;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/14

 * Contributors:
 *      - initial implementation
 */

import com.ucsmy.commons.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/8/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificationControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testQueryCa() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cert/ca"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testQueryCertPassword() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("cerCode", "100017081115000001");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/certificate/queryCertPassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.formatObjectToJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 预期返回值的媒体类型text/plain;charset=UTF-8
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateCertificate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        map.put("", "");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.formatObjectToJson(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
