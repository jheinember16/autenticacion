package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.r2dbc.entity.UserEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<User, UserEntity, String, MyReactiveRepository>
        implements UserRepository {

    private final MyReactiveRepository myReactiveRepository;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository,
                                       ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.myReactiveRepository = repository;
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user); // usa lógica de ReactiveAdapterOperations
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return myReactiveRepository.findByEmail(email)
                .map(entity -> super.toEntity(entity));
    }

    @Override
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return myReactiveRepository.findByDocumentNumber(documentNumber)
                .map(entity -> super.toEntity(entity));
    }

    @Override
    public Mono<User> findById(String idUser) {
        return super.findById(idUser);
    }

    @Override
    public Flux<User> findAll() {
        return super.findAll();
    }
}