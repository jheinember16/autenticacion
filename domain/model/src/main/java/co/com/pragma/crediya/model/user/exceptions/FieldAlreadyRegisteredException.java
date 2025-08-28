package co.com.pragma.crediya.model.user.exceptions;

public class FieldAlreadyRegisteredException extends RuntimeException {

    public FieldAlreadyRegisteredException(String fieldName) {
        super(fieldName + " is already registered");
    }
}