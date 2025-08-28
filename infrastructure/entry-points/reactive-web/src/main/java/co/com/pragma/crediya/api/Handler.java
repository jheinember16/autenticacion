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

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        log.debug("Entró en service findAll en UserHandler");
        return userUseCase.findAll()
                .collectList()
                .flatMap(listUser ->
                        ServerResponse.ok()
                                .bodyValue(userDtoMapper.toResponseList(listUser))
                );
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        log.debug("Entró en service findById en UserHandler");

        final String id = request.pathVariable("idUsuario");

        return userUseCase.findById(id)
                .flatMap(user ->
                        ServerResponse.ok()
                                .bodyValue(userDtoMapper.toResponse(user))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario {} no encontrado", id);
                    return ServerResponse.notFound().build();
                }));
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        log.debug("Entra service save en UserHandler");

        return serverRequest.bodyToMono(UserDTO.class) // Obtiene el cuerpo de la petición como Mono<UserDTO>
                .flatMap(validationHandler::validate) // Valida el DTO
                .map(userDtoMapper::toModel) // Convierte DTO -> Entidad User
                .flatMap(userUseCase::save) // Guarda el usuario
                .map(userDtoMapper::toResponse) // Convierte User -> UserResponse
                .flatMap(userCreate -> {
                    log.info("Usuario Creado: {}", userCreate.getIdUser());
                    return ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(userCreate);
                });
    }

    public Mono<ServerResponse> saveAll(ServerRequest serverRequest) {
        log.debug("Entra service saveAll en UserHandler");

        return serverRequest.bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {}) // Lista de UserDTO
                .flatMap(validationHandler::validateList) // Valida lista de DTOs
                .map(userDtoMapper::toModelList) // Convierte DTO -> List<User>
                .flatMapMany(userUseCase::saveAll) // Guarda todos los usuarios en flujo reactivo
                .map(userDtoMapper::toResponse) // Convierte cada User -> UserResponse
                .collectList() // Junta todos los UserResponse en una lista
                .flatMap(savedUsers -> {
                    log.info("Usuarios agregados: {}",
                            savedUsers.stream().map(UserDTO::getIdUser).collect(Collectors.toList()));
                    return ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(savedUsers);
                });
    }

    public Mono<ServerResponse> findByDocumentNumber(ServerRequest request) {
        log.debug("Entra service findByDocumentNumber en UserHandler");

        final String documentNumber = request.pathVariable("documentNumber");

        return userUseCase.findByDocumentNumber(documentNumber)
                .flatMap(user ->
                        ServerResponse.ok()
                                .bodyValue(userDtoMapper.toResponse(user))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario {} no encontrado", documentNumber);
                    return ServerResponse.noContent().build();
                }));
    }

}