package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.dto.questionbanl.CreateQuestIonBankPayLoad;
import com.spxam.test_service.entity.QuestionBank;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.service.IWriteQuestionBankService;
import com.spxam.test_service.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WriteQuestionBankServiceImpl implements IWriteQuestionBankService {

    private final IQuestionBankRepo iQuestionBankRepo;
    @Override
    public void createQuestionBank(CreateQuestIonBankPayLoad payLoad) {

        QuestionBank questionBank = QuestionBank.builder()
                .name(payLoad.name())
                .questionType(payLoad.questionType())
                .questionQnty(payLoad.questionQnty())
                .createdBy(payLoad.createdBy())
                .createdAt(CommonUtil.getCurrentDateTime())
                .isActive(true)
                .build();
        iQuestionBankRepo.save(questionBank);
    }
}
