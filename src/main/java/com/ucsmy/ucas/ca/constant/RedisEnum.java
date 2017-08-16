package com.ucsmy.ucas.ca.constant;

/**
 * redis常量
 *
 * @author ucs_guichang
 * @since 2017/07/25
 */
public enum RedisEnum {

    SPESTR(":", "redis名称分隔符"),
    TOKEN("token", "token常量"),
    TICKET("ticket", "ticket常量"),
    CLIENTNAME("client_name", "client_name常量"),
    CLIENTID("client_id", "client_id常量"),
    UCAS_CA("ucas_ca", "接口工程标识"),
    UCAS_CA_CLIENT("ucas_ca_client", "测试接口工程标识");

    private String value;
    private String descr;

    RedisEnum(String value, String descr) {
        this.value = value;
        this.descr = descr;
    }

    public String getValue() {
        return value;
    }

    public String getDescr() {
        return descr;
    }
}
