package az.erry.clinicrest.controllers;

import az.erry.clinicrest.models.RestResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().
                body(RestResponse.<String>builder().errors(e.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<RestResponse<String>> handleRestException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).
                body(RestResponse.<String>builder().errors(e.getReason()).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().
                body(RestResponse.<String>builder().errors(e.getMessage()).build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RestResponse<String>> handleIllegalArgumentException(IllegalStateException e) {
        return ResponseEntity.badRequest().
                body(RestResponse.<String>builder().errors(e.getMessage()).build());
    }
}
