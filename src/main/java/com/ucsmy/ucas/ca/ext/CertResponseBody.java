package com.ucsmy.ucas.ca.ext;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/7/31

 * Contributors:
 *      - initial implementation
 */

import java.io.Serializable;

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/7/31
 */
public class CertResponseBody implements Serializable {

    private String cerCode;
    private String cerType;
    private String domainName;
    private String country;
    private String province;
    private String city;
    private String orgName;
    private String orgUnitName;
    private String email;
    private String machineCode;
    private String createDate;
    private String createUser;
    private String deadTime;
    private String cerStatus;
    private String p12Secret;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getP12Secret() {
        return p12Secret;
    }

    public void setP12Secret(String p12Secret) {
        this.p12Secret = p12Secret;
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

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    public String getCerStatus() {
        return cerStatus;
    }

    public void setCerStatus(String cerStatus) {
        this.cerStatus = cerStatus;
    }
}
