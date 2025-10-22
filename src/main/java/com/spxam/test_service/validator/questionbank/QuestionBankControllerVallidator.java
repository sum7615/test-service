package com.spxam.test_service.validator.questionbank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.dto.questionbanl.CreateQuestIonBankPayLoad;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.ValidationResult;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class QuestionBankControllerVallidator {
    private final IUserFeign iUserFeign;
    private final IQuestionBankRepo iQuestionBankRepo;

    public ValidationResult validateCreateQuestionBankPayload(CreateQuestIonBankPayLoad payLoad) {

        List<String> errors = new ArrayList<>();

        CommonUtil.validateMandatory(payLoad.name(),"Name",errors);
        CommonUtil.validateMandatory(payLoad.createdBy(),"Created By",errors);
        if(payLoad.questionQnty() == null || payLoad.questionQnty() <= 0){
            errors.add("Question Quantity should be greater than zero.");
        }
        CommonUtil.validateMandatory(payLoad.questionType(),"Question Type",errors);

        UserDt userDt=null;
        try{
            userDt= iUserFeign.getUserByUserName(payLoad.createdBy());
        }catch (Exception e){
            errors.add("Not a valid user.");
        }

        Set<String> roles = userDt.roles()
                .stream()
                .map(String::toLowerCase)
                .collect(java.util.stream.Collectors.toSet());
        if(roles.isEmpty()){
            errors.add("No Roles Found for User");
        }
        if(roles.contains("user")){
            errors.add("Unauthorized Access");
        }

        iQuestionBankRepo.findByUserAndTime(payLoad.createdBy(), CommonUtil.getCurrentDateTime())
                .forEach(_ -> errors.add("Question Bank already created for user at this time."));

        return  new ValidationResult(errors.isEmpty(),errors);
    }
}
