package com.spxam.test_service.service;

import com.spxam.test_service.dto.questionbanl.CreateQuestIonBankPayLoad;

public interface IWriteQuestionBankService {
    void createQuestionBank(CreateQuestIonBankPayLoad payLoad);
}
