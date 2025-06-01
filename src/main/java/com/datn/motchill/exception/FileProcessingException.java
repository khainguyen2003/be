package com.datn.motchill.exception;

/**
 * Exception thrown when there is an error in file processing operations.
 */
public class FileProcessingException extends RuntimeException {
    
    public FileProcessingException(String message) {
        super(message);
    }
    
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
