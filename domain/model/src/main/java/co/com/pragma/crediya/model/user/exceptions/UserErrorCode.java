package co.com.pragma.crediya.model.user.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    INVALID_BIRTHDATE("USR_INVALID_BIRTHDATE", "Birth date must be in the past"),
    INVALID_SALARY("USR_INVALID_SALARY", "Base salary must be between 0.00 and 15,000,000.00"),
    DUPLICATE_EMAIL("USR_DUPLICATE_EMAIL", "Email address is already registered"),
    DUPLICATE_DOCUMENT("USR_DUPLICATE_DOCUMENT", "Document number is already registered");

    private final String code;
    private final String message;
}

