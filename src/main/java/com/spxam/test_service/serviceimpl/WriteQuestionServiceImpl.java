package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.dto.question.CreateQuestionPayload;
import com.spxam.test_service.entity.Question;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.IQuestionRepo;
import com.spxam.test_service.service.IWriteQuestionService;
import com.spxam.test_service.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WriteQuestionServiceImpl implements IWriteQuestionService {
    private final IQuestionRepo iQuestionRepo;
    private final IQuestionBankRepo iQuestionBankRepo;

    @Override
    public void createQuestion(CreateQuestionPayload payload) {

        var questIonBank = iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId()).orElse(null);

        Question qs = Question.builder()
                .title(payload.title())
                .problemStatement(payload.problemStatement())
                .problemStatementImg(payload.problemStatementImg())
                .o1(payload.o1())
                .o2(payload.o2())
                .o3(payload.o3())
                .o4(payload.o4())
                .o5(payload.o5())
                .ans(payload.ans())
                .marks(payload.mark())
                .type(payload.type())
                .createdBy(payload.createdBy())
                .createdAt(CommonUtil.getCurrentDateTime())
                .bankId(questIonBank)
                .isActive(true)
                .build();
        iQuestionRepo.save(qs);
    }
}
