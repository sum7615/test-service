package com.spxam.test_service.dto.test;

import java.time.LocalDateTime;

public record UpdateTestPayload(Long id,String name, String description, LocalDateTime startTime,
		LocalDateTime endTime,Long duration,String  userName,Long questionBankId,Long totalMarks,
		Long totalQuestions,Long passMark,String testLevel,Boolean isActive) {

}
