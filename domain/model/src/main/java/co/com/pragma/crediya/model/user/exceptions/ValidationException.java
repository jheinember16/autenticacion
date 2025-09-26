package co.com.pragma.crediya.model.user.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final UserErrorCode errorCode;

    public ValidationException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
