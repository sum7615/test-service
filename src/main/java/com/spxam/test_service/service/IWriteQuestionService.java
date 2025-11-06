package com.spxam.test_service.service;

import com.spxam.test_service.dto.question.CreateQuestionPayload;
import com.spxam.test_service.dto.question.DeleteQuestionPayload;
import com.spxam.test_service.dto.question.UpdateQuestionPayload;

public interface IWriteQuestionService {
    Long createQuestion(CreateQuestionPayload payload);

	void updateQuestion(UpdateQuestionPayload payload);

	void deleteQuestion(DeleteQuestionPayload payload);
}
