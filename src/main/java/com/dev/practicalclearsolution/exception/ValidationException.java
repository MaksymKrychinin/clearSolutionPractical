package com.dev.practicalclearsolution.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ValidationException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
}