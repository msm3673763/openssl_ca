package com.ucsmy.ucas.ca.utils;

/**
 * Created by ucs_liyuan on 2017/4/27.
 */
public class ThreadLocalUitls {

    /**
     * token
     */
    private static ThreadLocal<String> localToken = new ThreadLocal<>();

    public static void setToken(String token) {localToken.set(token);}

    public static String getToken() {return localToken.get();}

}
