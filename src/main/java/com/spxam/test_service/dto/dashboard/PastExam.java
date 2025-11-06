package com.spxam.test_service.dto.dashboard;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record PastExam(
		Long id,
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long totalMark,
        Long passMark,
        Long duration,
        LocalDateTime attemptStartTime,
        LocalDateTime attemptEndTime,
        Long obtainedMark,
        Long attemptedDuration,
        Long totalQsnAttempted,
        String status
) { }