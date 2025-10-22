package com.spxam.test_service.exception;

import jdk.jfr.MetadataDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;

@MetadataDefinition
@AllArgsConstructor
@Data
public class ExceptionResponse {
    private String status;
    private String message;
}