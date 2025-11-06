package com.spxam.test_service.service;

import java.util.List;

import com.spxam.test_service.dto.attempt.AttemptQstRes;
import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.ResumeTestRes;
import com.spxam.test_service.dto.attempt.StartTestDto;
import com.spxam.test_service.dto.attempt.ViewTestPayload;
import com.spxam.test_service.dto.attempt.ViewTestRes;

public interface IDoAttemptService {
    void attemptMcq(DoAttemptPayload payload);

    void startTest(StartTestDto payload);

    void finishTest(StartTestDto payload);

    AttemptQstRes getQuestion(StartTestDto payload);

	ViewTestRes getTestData(ViewTestPayload payload);

	List<ResumeTestRes> resumeTest(ViewTestPayload payload);
}
