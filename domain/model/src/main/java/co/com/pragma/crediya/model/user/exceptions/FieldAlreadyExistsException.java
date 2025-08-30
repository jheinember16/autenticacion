package co.com.pragma.crediya.model.user.exceptions;

public class FieldAlreadyExistsException extends RuntimeException {

    public FieldAlreadyExistsException(String fieldName) {
        super(fieldName + " that record already exists");
    }
}