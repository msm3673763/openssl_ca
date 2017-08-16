package com.ucsmy.ucas.ca.constant;

/**
 * 返回代码常量
 * Created by ucs_guichang on 2017/7/5.
 */
public enum ResultConstEnum {

    SUCCESS(1, "成功"),
    ERROR(-1, "失败"), //错误信息不可向前端用户展示
    PARAMS_ERROR(-2, "参数错误"),
    WARN_ERROR(-3, "异常提示"), //错误信息可向用户展示

    NOTICKET(-400, "ticket为空"),
    INVALIDTICKET(-401, "无效的ticket"),

    NO_RESULT(-501, "查询没有结果");

    private Integer value;
    private String descr;

    ResultConstEnum(Integer value, String descr) {
        this.value = value;
        this.descr = descr;
    }

    public String getDescr() {
        return descr;
    }

    public static String getDescr(int value) {
        String result = null;
        for (ResultConstEnum rc : ResultConstEnum.values()) {
            if (value == rc.getValue().intValue()) {
                result = rc.getDescr();
                break;
            }
        }
        return result;
    }

    public Integer getValue() {
        return value;
    }
}
