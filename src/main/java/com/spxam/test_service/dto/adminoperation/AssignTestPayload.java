package com.spxam.test_service.dto.adminoperation;

import lombok.Builder;

@Builder
public record AssignTestPayload(String assignTo, Long testId,String assignBy) {
}
