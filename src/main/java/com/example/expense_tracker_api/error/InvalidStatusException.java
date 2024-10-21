package com.example.expense_tracker_api.error;

public class InvalidStatusException extends BadRequestException {
    public static InvalidStatusException forError(ErrorCode errorCode, Object... args) {
        return new InvalidStatusException(errorCode, args);
    }

    private InvalidStatusException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
