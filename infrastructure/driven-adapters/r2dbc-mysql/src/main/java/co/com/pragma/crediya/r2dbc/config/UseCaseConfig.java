package co.com.pragma.crediya.r2dbc.config;

import co.com.pragma.crediya.model.user.common.TransactionPort;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository,
                                   TransactionPort transactionPort) {
        return new UserUseCase(userRepository, transactionPort);
    }
}
