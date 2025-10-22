package com.spxam.test_service.dto.dashboard;


import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record UpcomingExam(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long totalMark,
        Long passMark,
        Long duration
) { }

