package com.datn.motchill.exception;

/**
 * Exception thrown when there is an error in service operations.
 */
public class ServiceException extends RuntimeException {
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
