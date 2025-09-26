package co.com.pragma.crediya.model.user.gateways;

import co.com.pragma.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  UserRepository {

    Mono<User> save(User user);

    Mono<User> findByEmail(String email);

    Mono<User> findByDocumentNumber(String documentNumber);

    Mono<User> findById(String idUser);

    Flux<User> findAll();
}

