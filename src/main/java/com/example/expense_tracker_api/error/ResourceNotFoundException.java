package com.example.expense_tracker_api.error;

/**
 * Base class for exceptions that are thrown in response to attempts to
 * retrieve a specific resource from a database
 * or external system.
 * <p>
 * Handled by GlobalExceptionHandler (mapped to 404 response).
 */
public class ResourceNotFoundException extends RuntimeException {
    public static ResourceNotFoundException forError(ErrorCode errorCode, Object... args) {
        return new ResourceNotFoundException(errorCode, args);
    }

    private ResourceNotFoundException(ErrorCode apiErrorCode, Object... args) {
        super(String.format(apiErrorCode.getMessage(), args));
    }}
