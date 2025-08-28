package co.com.pragma.crediya.api.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Encargada de definir los atributos de error que se devuelven al cliente
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        Throwable error = getError(request);

        errorAttributes.put("timestamp", Instant.now().toString());
        errorAttributes.put("path", request.path());
        errorAttributes.put("error", error.getClass().getSimpleName());
        errorAttributes.put("message", error.getMessage());

        return errorAttributes;
    }
}
