package co.com.pragma.crediya.r2dbc.config;

import co.com.pragma.crediya.model.user.common.TransactionPort;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionAdapter implements TransactionPort {
    private final TransactionalOperator transactionalOperator;

    public <T> Mono<T> write(Supplier<Mono<T>> action) {
        return Mono.defer(action).as(transactionalOperator::transactional);
    }

    @Override
    public <T> Flux<T> writeMany(Supplier<Publisher<T>> work) {
        return Flux.defer(() -> Flux.from(work.get())).as(transactionalOperator::transactional);
    }
}
