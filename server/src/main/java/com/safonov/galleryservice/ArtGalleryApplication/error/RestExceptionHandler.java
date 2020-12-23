package com.safonov.galleryservice.ArtGalleryApplication.error;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.hibernate.JDBCException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NotNull final HttpMessageNotReadableException ex,
            @NotNull final HttpHeaders headers,
            @NotNull final HttpStatus status,
            @NotNull final WebRequest request) {
        final String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull final MethodArgumentNotValidException ex,
            @NotNull final HttpHeaders headers,
            @NotNull final HttpStatus status,
            @NotNull final WebRequest request) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        final Map<String, Set<String>> errorsMap = fieldErrors.stream().collect(
                Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())
                )
        );

        final ApiError apiError = new ApiError(status);
        apiError.setError("ARGUMENT_NOT_VALID");

        final ObjectWriter ow = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .writer().withDefaultPrettyPrinter();
        apiError.setMessage(errorsMap.toString());

        return new ResponseEntity<>(apiError, headers, status);
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> processJWTVerificationException(@NotNull final JWTVerificationException ex) {
        logger.debug("JWT is not verificated!");

        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setError("INVALID_JWT");
        apiError.setMessage("JWT token is not valid");

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(JDBCException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> processJDBCException(@NotNull final JDBCException ex) {
        logger.debug("processJDBCException is catched!");

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setError(getErrorFromJDBCException(ex.getSQLException().getMessage()));
        apiError.setMessage(getErrorFromJDBCException(ex.getSQLException().getMessage()));

        return buildResponseEntity(apiError);
    }

    /*@ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> processClientNotFoundException(ClientNotFoundException ex) {
        logger.debug("ClientNotFoundException is catched!");

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setError("NOT_FOUND");
        apiError.setMessage(ex.getMessage());

        return buildResponseEntity(apiError);
    }*/

    private String getErrorFromJDBCException(@NotNull final String message) {
        String tmp = "";
        if (message.contains("already exists")) {
            tmp = message.substring(message.lastIndexOf("Key (") + "Key (".length());
            tmp = tmp.substring(0, tmp.indexOf(")"));
            return tmp.toUpperCase() + "_ALREADY_USED";
        }
        return "";
    }

    private ResponseEntity<Object> buildResponseEntity(@NotNull final ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
