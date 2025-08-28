package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.common.TransactionPort;
import co.com.pragma.crediya.model.user.exceptions.FieldAlreadyRegisteredException;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    private final UserRepository userRepository;
    private final TransactionPort transactionPort;

    @Override
    public Mono<User> findById(String idUser) {
        return userRepository.findById(idUser);
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<User> findByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber);
    }

    public Flux<User> saveAll(List<User> users) {
        return transactionPort.writeMany(() ->
                Flux.fromIterable(users)
                        .flatMap(this::save) // reutilizamos el save individual
        );
    }

    public Mono<User> save(User user) {
        return transactionPort.write(() ->
                findByEmail(user.getEmail())
                        .flatMap(existingUser ->
                                Mono.<User>error(new FieldAlreadyRegisteredException("email"))
                        )
                        .switchIfEmpty(
                                findByDocumentNumber(user.getDocumentNumber())
                                        .flatMap(existingUser ->
                                                Mono.<User>error(new FieldAlreadyRegisteredException("documentNumber"))
                                        )
                                        .switchIfEmpty(
                                                // Guardamos el usuario dentro de la transacción
                                                userRepository.save(user)
                                        )
                        )
        );
    }
}