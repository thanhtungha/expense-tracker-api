package com.example.expense_tracker_api.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The ApiErrorCode is used to hold data on Strings constants used
 * this gives much control in terms of changing string logs without the need to go through all classes
 */
@Getter
@RequiredArgsConstructor
@Schema(name = "ErrorCode", implementation = ErrorCode.class)
public enum ErrorCode {
    UNKNOWN_ERROR("Unknown error"),
    RESOURCE_NOT_FOUND_DEFAULT_MESSAGE("%s resource with id [%s] does not exist"),
    INVALID_EXPENSE_STATUS("[%s] is not a valid status");

    private final String message;
}
