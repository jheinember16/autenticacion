package co.com.pragma.crediya.shared.utilities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

@Component
public class ValidationHandler {

    private final Validator validator;

    public ValidationHandler(Validator validator) {
        this.validator = validator;
    }

    // Validate a single object
    public <T> Mono<T> validate(T object) {
        return Mono.defer(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            if (!violations.isEmpty()) {
                return Mono.error(new ConstraintViolationException(violations));
            }
            return Mono.just(object);
        });
    }

    // Validate a list of objects
    public <T> Mono<List<T>> validateList(List<T> list) {
        for (T item : list) {
            Set<ConstraintViolation<T>> violations = validator.validate(item);
            if (!violations.isEmpty()) {
                return Mono.error(new ConstraintViolationException(violations));
            }
        }
        return Mono.just(list);
    }
}
