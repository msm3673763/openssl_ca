package com.ucsmy.ucas.ca.entity;

/**
 *
 * @Description: 证书model
 * @author ucs_masiming
 * @date 2017/4/28 11:41
 * @version V1.0
 */
public class CertModel {

    private String validity;
    private String keyName;
    private String keyPass;
    private String caName;
    private String csrName;
    private String crtName;
    private String p12Name;
    private String country;
    private String province;
    private String city;
    private String company;
    private String unit;
    private String common;
    private String machineCode;
    private String certCode;

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getCrtName() {
        return crtName;
    }

    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public String getCsrName() {
        return csrName;
    }

    public void setCsrName(String csrName) {
        this.csrName = csrName;
    }

    public String getP12Name() {
        return p12Name;
    }

    public void setP12Name(String p12Name) {
        this.p12Name = p12Name;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
