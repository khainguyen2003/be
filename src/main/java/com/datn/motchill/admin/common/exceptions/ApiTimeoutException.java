package com.datn.motchill.admin.common.exceptions;

public class ApiTimeoutException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6642731651400770229L;
	private final String errorCode;

    public ApiTimeoutException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
