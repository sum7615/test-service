package com.spxam.test_service.service;

import com.spxam.test_service.dto.questionbanl.QuestionBankLookUpRes;

import java.util.List;

public interface IReadQuestionBankService {
    List<QuestionBankLookUpRes> fetchQuestionBanks(String userName);
}
