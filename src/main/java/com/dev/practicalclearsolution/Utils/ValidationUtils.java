package com.dev.practicalclearsolution.Utils;

import com.dev.practicalclearsolution.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationUtils {
    private final Validator validator;

    public <T> void validate(T entity) {
        if (entity != null) {
            Set<ConstraintViolation<T>> validateResult = validator.validate(entity);
            if (!validateResult.isEmpty()) {
                String allValidateTroubles = validateResult.stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .get();
                throw ValidationException.builder()
                        .message(allValidateTroubles)
                        .httpStatus(HttpStatus.BAD_REQUEST).build();
            }
        }
    }
}
