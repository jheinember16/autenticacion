package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.ApiResponse;
import co.com.pragma.crediya.api.dto.UserDTO;
import co.com.pragma.crediya.api.mapper.UserDtoMapper;
import co.com.pragma.crediya.shared.utilities.ValidationHandler;
import co.com.pragma.crediya.usecase.user.SaveUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final SaveUserUseCase saveUserUseCase;
    private final UserDtoMapper userDtoMapper;
    private final ValidationHandler validationHandler;

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        log.info("Creando un nuevo usuario");

        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(validationHandler::validate)
                .map(userDtoMapper::toModel)
                .flatMap(saveUserUseCase::save)
                .map(userDtoMapper::toResponse)
                .flatMap(userCreated -> {
                    log.info("Usuario creado correctamente con ID: {}", userCreated.getId());

                    ApiResponse<Object> apiResponse = ApiResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message("Usuario creado exitosamente")
                            .data(userCreated)
                            .build();

                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(apiResponse);
                });
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        log.info("Solicitando lista de todos los usuarios");
        return saveUserUseCase.findAll()
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

        return saveUserUseCase.findById(id)
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

    public Mono<ServerResponse> findByDocumentNumber(ServerRequest request) {
        final String documentNumber = request.pathVariable("documentNumber");
        log.info("Buscando usuario con número de documento: {}", documentNumber);

        return saveUserUseCase.findByDocumentNumber(documentNumber)
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