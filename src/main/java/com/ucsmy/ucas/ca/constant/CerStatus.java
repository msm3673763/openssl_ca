package com.ucsmy.ucas.ca.constant;

public enum CerStatus {

    VALID("1", "有效"),  //有效
    INVALID("2", "无效");  //无效
    private final String index;
    private final String value;
    CerStatus(final String index, final String value){
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