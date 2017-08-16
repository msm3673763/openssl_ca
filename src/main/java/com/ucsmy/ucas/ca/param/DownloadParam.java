package com.ucsmy.ucas.ca.param;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/4

 * Contributors:
 *      - initial implementation
 */

/**
 * 下载证书接口请求参数
 *
 * @author ucs_masiming
 * @since 2017/8/4
 */
public class DownloadParam {

    private String ticket;
    private String cerCode;
    private String fileType;

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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
