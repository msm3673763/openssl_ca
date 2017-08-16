package com.ucsmy.ucas.ca.param;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/4

 * Contributors:
 *      - initial implementation
 */

/**
 * 吊销接口请求参数
 *
 * @author ucs_masiming
 * @since 2017/8/4
 */
public class RequestParam {

    private String ticket;
    private String cerCode;
    private String cerType;

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

    public String getCerType() {
        return cerType;
    }

    public void setCerType(String cerType) {
        this.cerType = cerType;
    }
}
