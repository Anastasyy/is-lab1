package itmo.is.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex) {
        return "Entity not found: " + ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return "Illegal argument: " + ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex) {
        return "Illegal state: " + ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageConversionException.class, MethodArgumentTypeMismatchException.class})
    public String handleInvalidRequestExceptions(Exception ex) {
        return "Invalid request: " + ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex) {
        return "Cannot authenticate: " + ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public String handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(SQLException.class)
    public String handleSQLException(Exception ex) {
        return "Database error occurred. Please contact support if the issue persists.";
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(LockAcquisitionException.class)
    public String handleLockAcquisitionException(LockAcquisitionException ex) {
        return "The resource is currently locked by another operation";
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(StaleObjectStateException.class)
    public String handleStaleObjectStateException(StaleObjectStateException ex) {
        return "The resource has been updated or deleted by another transaction";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return "Exception: " + ex.getMessage();
    }
}
