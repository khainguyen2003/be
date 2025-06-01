package com.datn.motchill.common.exceptions;

public class NotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1818166159737162016L;

	public NotFoundException(String message) {
        super(message);
    }
}
