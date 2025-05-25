package com.datn.motchill.admin.common.exceptions;

public class BadRequestException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3962664778766538060L;

	public BadRequestException(String message) {
        super(message);
    }

}
