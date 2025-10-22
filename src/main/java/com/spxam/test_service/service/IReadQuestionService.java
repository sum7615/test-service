package com.spxam.test_service.service;

import com.spxam.test_service.dto.question.QuestionDataRes;

import java.util.List;

public interface IReadQuestionService {
    List<QuestionDataRes> getQuestions(String userName, Long questionBankId);
}
