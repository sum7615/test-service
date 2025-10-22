package com.spxam.test_service.service;

import com.spxam.test_service.dto.question.CreateQuestionPayload;

public interface IWriteQuestionService {
    void createQuestion(CreateQuestionPayload payload);
}
