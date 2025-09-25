package co.com.pragma.crediya.shared.utilities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import jakarta.validation.Validator;
import java.util.Set;

@Component
public record ValidationHandler(Validator validator) {

    public <T> Mono<T> validate(T object) {
        return Mono.defer(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            if (!violations.isEmpty()) {
                return Mono.error(new ConstraintViolationException(violations));
            }
            return Mono.just(object);
        });
    }
}
