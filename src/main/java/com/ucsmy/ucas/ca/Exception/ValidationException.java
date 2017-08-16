package com.ucsmy.ucas.ca.Exception;

/**
 *
 * @Description: 验证自定义异常
 * @author ucs_masiming
 * @date 2017/4/28 15:44
 * @version V1.0
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable t) {
        super(msg, t);
    }
}
