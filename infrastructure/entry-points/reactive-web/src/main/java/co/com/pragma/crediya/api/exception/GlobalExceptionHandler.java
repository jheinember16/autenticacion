package co.com.pragma.crediya.api.exception;

import co.com.pragma.crediya.api.dto.ApiResponse;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;
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

        int statusCode = (int) errorProperties.get("status");
        HttpStatus status = HttpStatus.valueOf(statusCode);

        log.error("Error on {}: {}", request.path(), errorProperties.get("message"));

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .status(statusCode)
                .message((String) errorProperties.get("message"))
                .errors((List<String>) errorProperties.get("errors"))
                .build();

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(apiResponse));
    }
}
