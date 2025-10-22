package com.spxam.test_service.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationResult {
    private final boolean valid;
    private final List<String> errors;
}
