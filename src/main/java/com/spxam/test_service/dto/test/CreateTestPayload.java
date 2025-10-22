package com.spxam.test_service.dto.test;

import java.time.LocalDateTime;

public record CreateTestPayload(String name, String description, LocalDateTime startTime,LocalDateTime endTime,Long duration,String  createdBy,Long questionBankId,Long totalMarks) {
}
