package com.spxam.test_service.dto.attempt;

import java.time.LocalDateTime;

import lombok.Builder;
@Builder
public record ViewTestRes(Long id, String name, String description,LocalDateTime assignedAt,LocalDateTime startTime,
		LocalDateTime endTime,Long duration,Long totalMark, Long totalQuestions,Long passMark,Boolean isStarted
		) {

}
