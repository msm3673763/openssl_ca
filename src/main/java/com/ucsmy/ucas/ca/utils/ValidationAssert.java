package com.ucsmy.ucas.ca.utils;

import com.ucsmy.ucas.ca.Exception.ValidationException;
import com.ucsmy.ucas.commons.aop.exception.BusinessException;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @Description: 断言工具类，断言失败抛出ValidationException
 * @author ucs_masiming
 * @date 2017/4/28 15:42
 * @version V1.0
 */
public class ValidationAssert {
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (str == null || str.trim().length() == 0) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(Object[] arrays, String message) {
        if (arrays == null || arrays.length == 0) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(Collection<?> c, String message) {
        if (c ==  null || c.isEmpty()) {
            throw new ValidationException(message);
        }
    }

    public static void notEmpty(@SuppressWarnings("rawtypes") Map map, String message) {
        if (map ==  null || map.isEmpty()) {
            throw new ValidationException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ValidationException(message);
        }
    }

    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new ValidationException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        notNull(type, "Type to check against must not be null.");

        if (type.isInstance(obj)) {
            throw new ValidationException("Object of class ["
                    + (obj != null ? obj.getClass().getName() : "null")
                    + "] must be an instance of " + type);
        }
    }

    public static void fail(String message) {
        throw new ValidationException(message);
    }

    public static void equals(Object o1, Object o2, String message) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null) {
            throw new BusinessException(message);
        }
        if (!o1.equals(o2)) {
            throw new BusinessException(message);
        }
    }

    public static void isAmount(String text, String message) {
        Pattern pattern = Pattern.compile("^-?[\\d]+(\\.[\\d]{1,2})?$");
        Matcher m = pattern.matcher(text);
        if (!m.matches()) {
            throw new ValidationException(message);
        }
    }

    public static void main(String[] args) {
        String a = "0.01";
        isAmount(a, "error");
        a = "1.00";
        isAmount(a, "error");
        a = "-23";
        isAmount(a, "error");
        System.out.println("ok");
    }
}
