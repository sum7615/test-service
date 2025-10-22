package com.spxam.test_service.dto.dashboard;

import java.util.List;

import lombok.Builder;
@Builder
public record DashBoardRes( List<UpcomingExam> upcoming,
                            List<PastExam> past) {
}
