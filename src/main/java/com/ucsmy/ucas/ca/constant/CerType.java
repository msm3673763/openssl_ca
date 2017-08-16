package com.ucsmy.ucas.ca.constant;

public enum CerType {

    SERVER("1", "应用服务器证书"),  //应用服务器证书
    CLIENT("2", "终端证书"),  //终端证书
    ROOT("3", "ca根证书");      //ca根证书
    private final String index;
    private final String value;
    CerType(final String index, final String value){
        this.index = index;
        this.value = value;
    }
    @Override
    public String toString(){
        return index;
    }

    public String getIndex() {
        return index;
    }
}