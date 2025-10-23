package com.spxam.test_service.dto.attempt;

public record DoAttemptPayload(String userName, Long questionId,Long testId, String ans,Long timeTakenInSeconds) {
}
