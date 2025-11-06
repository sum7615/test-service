package com.spxam.test_service.service;

import com.spxam.test_service.dto.questionbanl.CreateQuestIonBankPayLoad;
import com.spxam.test_service.dto.questionbanl.DeleteQuestionBankPayload;
import com.spxam.test_service.dto.questionbanl.UpdateQuestionBankPayload;

public interface IWriteQuestionBankService {
    Long createQuestionBank(CreateQuestIonBankPayLoad payLoad);

	void updateQuestionBank(UpdateQuestionBankPayload payLoad);

	void deleteQuestionBank(DeleteQuestionBankPayload payLoad);
}
