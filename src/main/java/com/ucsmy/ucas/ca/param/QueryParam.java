package com.ucsmy.ucas.ca.param;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/4

 * Contributors:
 *      - initial implementation
 */

/**
 * 获取p12证书密码请求参数
 *
 * @author ucs_masiming
 * @since 2017/8/4
 */
public class QueryParam {

    private String ticket;
    private String cerCode;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getCerCode() {
        return cerCode;
    }

    public void setCerCode(String cerCode) {
        this.cerCode = cerCode;
    }
}
