package com.spxam.test_service.dto.test;

import java.time.LocalDateTime;

import lombok.Builder;
@Builder
public record FetchTestAdmin(Long id,String name, String description, LocalDateTime startTime,
		LocalDateTime endTime,Long duration,String  createdBy,Long questionBankId,Long totalMarks,
		Long totalQuestions,Long passMark,String testLevel) {

}
