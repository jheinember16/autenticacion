package co.com.pragma.crediya.api.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Map;


@Component
@Slf4j
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer configurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.setMessageReaders(configurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorProperties =
                getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Throwable error = getError(request);
        HttpStatus status = getHttpStatus(error);
        log.error("Error en {}: {}", request.path(), error.getMessage());
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorProperties));
    }

    private HttpStatus getHttpStatus(Throwable error) {
        if (error instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        } else if (error instanceof NullPointerException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (error instanceof ValidationException) {
            return HttpStatus.BAD_REQUEST;
        } else if (error instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (error instanceof MethodArgumentNotValidException) {
            return HttpStatus.BAD_REQUEST;
        } else if (error instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}