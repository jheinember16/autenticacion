package co.com.pragma.crediya.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;
import jakarta.validation.ValidationException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(request);
        HttpStatus status = getHttpStatus(error);

        errorAttributes.put("status", status.value());
        errorAttributes.put("message", resolveMessage(error));

        if (status == HttpStatus.BAD_REQUEST) {
            errorAttributes.put("errors", extractErrors(error));
        }

        errorAttributes.put("path", request.path());

        return errorAttributes;
    }

    private HttpStatus getHttpStatus(Throwable error) {
        if (error instanceof IllegalArgumentException
                || error instanceof ValidationException
                || error instanceof MethodArgumentNotValidException
                || error instanceof InvalidFormatException
                || error instanceof DateTimeParseException
                || error instanceof ServerWebInputException) {
            return HttpStatus.BAD_REQUEST;
        } else if (error instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (error instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveMessage(Throwable error) {
        if (error instanceof ServerWebInputException swie) {
            Throwable cause = swie.getCause();
            if (cause instanceof InvalidFormatException || cause instanceof DateTimeParseException) {
                return "Invalid date format. Please use yyyy-MM-dd";
            }
            return swie.getReason() != null ? swie.getReason() : "Invalid request";
        } else if (error instanceof InvalidFormatException || error instanceof DateTimeParseException) {
            return "Invalid date format. Please use yyyy-MM-dd";
        } else if (error instanceof MethodArgumentNotValidException) {
            return "Validation failed";
        }
        return error.getMessage() != null ? error.getMessage() : "Unexpected error occurred";
    }

    private List<String> extractErrors(Throwable error) {
        if (error instanceof MethodArgumentNotValidException ex) {
            return ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(f -> f.getField() + ": " + f.getDefaultMessage())
                    .collect(Collectors.toList());
        } else if (error instanceof ServerWebInputException swie) {
            Throwable cause = swie.getCause();
            if (cause instanceof InvalidFormatException ife && !ife.getPath().isEmpty()) {
                return List.of("Invalid value for field '" + ife.getPath().get(0).getFieldName() + "'");
            }
            return List.of(resolveMessage(error));
        } else {
            return List.of(resolveMessage(error));
        }
    }
}
