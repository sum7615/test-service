package com.spxam.test_service.converter;

import com.spxam.test_service.dto.questionbanl.QuestionBankLookUpRes;
import com.spxam.test_service.entity.QuestionBank;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionBankToDtoConverter {
    public List<QuestionBankLookUpRes> toDtoList(List<QuestionBank> questionBank){
        return questionBank.stream().map(q->{
            return new QuestionBankLookUpRes(q.getName(),q.getId(),q.getQuestionType(),q.getDescription());
        }).toList();
    }
}
