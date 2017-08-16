package com.ucsmy.ucas.ca.constant;

/**
 * Created by ucs_zhengfucheng on 2017/5/2.
 */
public enum FileSuffix {

    KEY("key"),
    CRT("crt"),
    CSR("csr"),
    P12("p12"),
    CRL("crl"),
    JKS("jks");

    private String suffix;


    FileSuffix(final String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "CerSuffix{" +
                "suffix='" + suffix + '\'' +
                '}';
    }

    public String getSuffix() {
        return suffix;
    }
}
