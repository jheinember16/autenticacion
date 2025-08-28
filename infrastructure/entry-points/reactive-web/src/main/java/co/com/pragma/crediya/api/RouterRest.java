package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.boot.autoconfigure.web.WebProperties;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "findAll",
                    operation = @Operation(
                            operationId = "findAll",
                            summary = "Lista todos los usuarios",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuarios obtenidos correctamente",
                                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{idUsuario}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            summary = "Busca un usuario por ID",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "idUsuario",
                                            description = "Identificador único del usuario",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/findByDocumentNumber/{documentNumber}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "findByDocumentNumber",
                    operation = @Operation(
                            operationId = "findByDocumentNumber",
                            summary = "Busca un usuario por número de documento",
                            parameters = {
                                    @Parameter(
                                            in = ParameterIn.PATH,
                                            name = "documentNumber",
                                            description = "Número de documento del usuario",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "No se encontró usuario con ese documento")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "save",
                            summary = "Registra un nuevo usuario",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Usuario registrado exitosamente",
                                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }
                    )
            ),@RouterOperation(
            path = "/api/v1/usuarios/all",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "saveAll",
            operation = @Operation(
                    operationId = "saveAll",
                    summary = "Registra múltiples usuarios",
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Usuarios registrados exitosamente",
                                    content = @Content(schema = @Schema(implementation = UserDTO.class))
                            ),
                            @ApiResponse(responseCode = "400", description = "Error en validación de datos")
                    }
            )
    )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/usuarios"), handler::findAll)
                .andRoute(GET("/api/v1/usuarios/{idUsuario}"), handler::findById)
                .andRoute(GET("/api/v1/usuarios/findByDocumentNumber/{documentNumber}"), handler::findByDocumentNumber)
                .andRoute(POST("/api/v1/usuarios"), handler::save)
                .andRoute(POST("/api/v1/usuarios/all"), handler::saveAll);
    }
}






