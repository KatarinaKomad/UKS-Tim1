package uns.ac.rs.uks.apiResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import uns.ac.rs.uks.apiResponse.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ControllerAdvisor {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        e.getBindingResult().getGlobalErrors().forEach(error ->
            errors.put(error.getObjectName(), error.getDefaultMessage())
        );

        return new ResponseError(HttpStatus.BAD_REQUEST, "Field validation failed.", errors);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedException(Exception e) {
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.FORBIDDEN);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationException(Exception e) {
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unknownError(Exception e) {
        e.printStackTrace();
        log.error("Unknown error: {}", e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
