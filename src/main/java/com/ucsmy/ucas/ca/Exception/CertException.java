package com.ucsmy.ucas.ca.Exception;

/**
 *
 * @Description: 自定义异常
 * @author ucs_masiming
 * @date 2017/4/27 10:24
 * @version V1.0
 */
public class CertException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CertException(String message) {
        super(message);
    }

    public CertException(String msg, Throwable t) {
        super(msg, t);
    }
}
