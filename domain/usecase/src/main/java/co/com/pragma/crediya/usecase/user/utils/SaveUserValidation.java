package co.com.pragma.crediya.usecase.user.utils;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.exceptions.ValidationException;
import co.com.pragma.crediya.model.user.exceptions.UserErrorCode;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SaveUserValidation {

    private final UserRepository userRepository;

    public SaveUserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Void> validateUser(User user) {
        return validateBirthDate(user.getBirthDate())
                .then(validateBaseSalary(user.getBaseSalary()))
                .then(validateUniqueEmail(user.getEmail()));
    }

    private Mono<Void> validateBirthDate(LocalDate birthDate) {
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            return Mono.error(new ValidationException(UserErrorCode.INVALID_BIRTHDATE));
        }
        return Mono.empty();
    }

    private Mono<Void> validateBaseSalary(BigDecimal baseSalary) {
        if (baseSalary == null ||
                baseSalary.compareTo(BigDecimal.ZERO) <= 0 ||
                baseSalary.compareTo(new BigDecimal("15000000.00")) > 0) {
            return Mono.error(new ValidationException(UserErrorCode.INVALID_SALARY));
        }
        return Mono.empty();
    }

    private Mono<Void> validateUniqueEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(existingUser -> Mono.error(new ValidationException(UserErrorCode.DUPLICATE_EMAIL)))
                .then();
    }
}