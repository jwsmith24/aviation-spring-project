package mil.army.swf.aviationappspring.util.http;

import lombok.extern.slf4j.Slf4j;
import mil.army.swf.aviationappspring.util.http.exceptions.BadRequestException;
import mil.army.swf.aviationappspring.util.http.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {

        log.error("Resource not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        log.error("Bad request: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(new ValidationErrorResponse("BAD_REQUEST",
                "Validation failed for incoming request", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "Something went wrong.."));
    }


}
