package com.example.aistudio.web;

import com.example.aistudio.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(SessionService.NotFoundException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleNotFound(SessionService.NotFoundException ex, HttpServletRequest req) {
        return respond(HttpStatus.NOT_FOUND, ex.getCode(), ex.getMessage(), null, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> details = new LinkedHashMap<>();
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                            .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                            .toList();
        details.put("fieldErrors", fieldErrors);
        return respond(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Validation failed", details, req);
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<ApiError> handleUnknown(Exception ex, HttpServletRequest req) {
        return respond(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage(), null, req);
    }

    private org.springframework.http.ResponseEntity<ApiError> respond(HttpStatus status, String code, String message, Map<String, Object> details, HttpServletRequest req) {
        String requestId = getOrCreateRequestId(req);
        var err = new ApiError(Instant.now(), requestId, code, message, details);
        return org.springframework.http.ResponseEntity.status(status).body(err);
    }

    private String getOrCreateRequestId(HttpServletRequest req) {
        var rid = req.getHeader("X-Request-Id");
        return (rid == null || rid.isBlank()) ? UUID.randomUUID().toString() : rid;
    }

}
