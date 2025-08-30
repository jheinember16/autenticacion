package co.com.pragma.crediya.usecase.user;


import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.common.TransactionPort;
import co.com.pragma.crediya.model.user.exceptions.FieldAlreadyExistsException;
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
    public Mono<User> save(User user) {
        return transactionPort.write(() ->
                userRepository.findByEmail(user.getEmail())
                        .flatMap(existing -> Mono.<User>error(new FieldAlreadyExistsException("email")))
                        .switchIfEmpty(userRepository.findByDocumentNumber(user.getDocumentNumber())
                        .flatMap(existing -> Mono.<User>error(new FieldAlreadyExistsException("documentNumber")))
                        .switchIfEmpty(userRepository.save(user)))
        );
    }

    @Override
    public Flux<User> saveAll(List<User> users) {
        return transactionPort.writeMany(() ->
                Flux.fromIterable(users).flatMap(this::save)
        );
    }

    @Override
    public Mono<User> findById(String idUser) {
        return userRepository.findById(idUser);
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber);
    }
}
