package com.course.courseapp.exception;

import com.course.courseapp.util.CourseApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CourseApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        log.warn("Validation failed: {}", errors);

        CourseApiResponse<Map<String, String>> response =
                new CourseApiResponse<>(false, "Validation failed", errors, HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CourseApiResponse<String>> handleBusinessErrors(IllegalStateException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());

        CourseApiResponse<String> response =
                new CourseApiResponse<>(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CourseApiResponse<String>> handleAllUncaught(Exception ex) {
        log.error("Unexpected error", ex);

        CourseApiResponse<String> response =
                new CourseApiResponse<>(false, "An unexpected error occurred. Please try again later.", null, HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
