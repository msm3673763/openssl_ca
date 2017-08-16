package com.ucsmy.ucas.ca.param;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/4

 * Contributors:
 *      - initial implementation
 */

/**
 * 分页查询接口请求参数
 *
 * @author ucs_masiming
 * @since 2017/8/4
 */
public class PageParam {

    private String ticket;
    private String cerType;
    private int pageNum;
    private int pageSize;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getCerType() {
        return cerType;
    }

    public void setCerType(String cerType) {
        this.cerType = cerType;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
