package co.com.pragma.crediya.api;


import co.com.pragma.crediya.api.dto.UserDTO;
import co.com.pragma.crediya.api.mapper.UserDtoMapper;
import co.com.pragma.crediya.shared.utilities.ValidationHandler;
import co.com.pragma.crediya.usecase.user.IUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final IUserUseCase userUseCase;
    private final UserDtoMapper userDtoMapper;
    private final ValidationHandler validationHandler;

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        log.info("Creando un nuevo usuario");
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(validationHandler::validate)
                .map(userDtoMapper::toModel)
                .flatMap(userUseCase::save)
                .map(userDtoMapper::toResponse)
                .flatMap(userCreate -> {
                    log.info("Usuario creado correctamente con ID: {}", userCreate.getIdUser());
                    return ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(userCreate);
                });
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        log.info("Solicitando lista de todos los usuarios");
        return userUseCase.findAll()
                .collectList()
                .flatMap(listUser ->
                        ServerResponse.ok()
                                .bodyValue(userDtoMapper.toResponseList(listUser))
                                .doOnSuccess(resp -> log.info("Se obtuvieron {} usuarios", listUser.size()))
                );
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        final String id = request.pathVariable("idUsuario");
        log.info("Buscando usuario con ID: {}", id);

        return userUseCase.findById(id)
                .flatMap(user ->
                        ServerResponse.ok()
                                .bodyValue(userDtoMapper.toResponse(user))
                                .doOnSuccess(resp -> log.info("Usuario encontrado: {}", id))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario con ID {} no encontrado", id);
                    return ServerResponse.notFound().build();
                }));
    }


    public Mono<ServerResponse> saveAll(ServerRequest serverRequest) {
        log.info("Creando múltiples usuarios desde la petición");

        return serverRequest.bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(validationHandler::validateList)
                .map(userDtoMapper::toModelList)
                .flatMapMany(userUseCase::saveAll)
                .map(userDtoMapper::toResponse)
                .collectList()
                .flatMap(savedUsers -> {
                    log.info("Se agregaron {} usuarios correctamente: {}",
                            savedUsers.size(),
                            savedUsers.stream().map(UserDTO::getIdUser).collect(Collectors.toList()));
                    return ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(savedUsers);
                });
    }

    public Mono<ServerResponse> findByDocumentNumber(ServerRequest request) {
        final String documentNumber = request.pathVariable("documentNumber");
        log.info("Buscando usuario con número de documento: {}", documentNumber);

        return userUseCase.findByDocumentNumber(documentNumber)
                .flatMap(user ->
                        ServerResponse.ok()
                                .bodyValue(userDtoMapper.toResponse(user))
                                .doOnSuccess(resp -> log.info("Usuario encontrado con documento: {}", documentNumber))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No se encontró usuario con documento: {}", documentNumber);
                    return ServerResponse.notFound().build();
                }));
    }

}
