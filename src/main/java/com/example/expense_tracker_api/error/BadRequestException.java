package com.example.expense_tracker_api.error;

/**
 * Base class for exceptions that are thrown in response to an invalid request payload.
 * <p>
 * Handled by GlobalExceptionHandler (mapped to 400 response).
 */
public abstract class BadRequestException extends RuntimeException {
    public BadRequestException(ErrorCode apiErrorCode, Object... args) {
        super(String.format(apiErrorCode.getMessage(), args));
    }
}
