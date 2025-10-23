package com.spxam.test_service.service;

import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.StartTestDto;

public interface IDoAttemptService {
    void attemptMcq(DoAttemptPayload payload);

    void startTest(StartTestDto payload);

    void finishTest(StartTestDto payload);
}
