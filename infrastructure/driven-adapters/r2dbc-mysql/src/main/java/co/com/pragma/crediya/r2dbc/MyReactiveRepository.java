package co.com.pragma.crediya.r2dbc;


import co.com.pragma.crediya.r2dbc.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByEmail(String email);

    Mono<UserEntity> findByDocumentNumber(String documentNumber);
}