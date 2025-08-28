package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserUseCase {

    Mono<User> save(User user);

    Mono<User> findByEmail(String email);

    Mono<User> findByDocumentNumber(String documentNumber);

    Mono<User> findById(String idUser);

    Flux<User> findAll();

    Flux<User> saveAll(List<User> users);
}
