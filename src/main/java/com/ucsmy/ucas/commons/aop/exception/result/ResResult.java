package com.ucsmy.ucas.commons.aop.exception.result;

/**
 * Created by ucs_liyuan on 2017/4/11.
 */
public class ResResult {
    private int res;
    private String des;
    private Object data;


    public enum ResultType{
        /*
        1	成功
        -1	失败，错误信息“不可以”向用户展示
        -2	参数错误
        -3	失败，错误信息“可以”向用户展示
        */
        SUCCESS("成功", 1), OTHER_FAILURE("失败", -1), PARAMETER_ERROR("参数错误", -2), FAILURE("失败", -3),
        QUERY_NO_RESULT("查询无结果", -501), TOKEN_EXPIRE("token过期", 40002), TOKEN_INVAILD("无效token",40004),
        SERVER_ERROR("统一认证服务错误",40016);
        private String name ;
        private int index ;

        ResultType( String name , int index ){
            this.name = name ;
            this.index = index ;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
    }

    public ResResult() {

    }

    public ResResult(int res, String des, Object data) {
        this.res = res;
        this.des = des;
        this.data = data;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static ResResult retFailureMsg(ResultType type, String des) {
        return new ResResult(type.index, des, null);
    }

    public static ResResult retFailureMsg(ResultType type) {
        return new ResResult(type.index, type.name, null);
    }

    public static ResResult retFailureMsg(String des, Object data) {
        return new ResResult(ResultType.FAILURE.index, des, data);
    }

    public static ResResult retSuccessMsg(String des, Object data) {
        return new ResResult(ResultType.SUCCESS.index, des, data);
    }

    public static ResResult retSuccessMsg(String des) {
        return new ResResult(ResultType.SUCCESS.index, des, null);
    }

}
