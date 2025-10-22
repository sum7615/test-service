package com.spxam.test_service.validator.test;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.converter.TestToDtoConverter;
import com.spxam.test_service.dto.test.CreateTestPayload;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.entity.TestUserMap;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.exception.NotAuthorizedAccess;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.ValidationResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class TestControllerValidator {
    private final IUserFeign iUserFeign;
    private final ITestRepo iTestRepo;
    private final IQuestionBankRepo iQuestionBankRepo;

    public ValidationResult vaidationCreateTestPayload(CreateTestPayload payload) {

        List<String> errors = new ArrayList<>();
        UserDt userDt=null;
        try{
            userDt= iUserFeign.getUserByUserName(payload.createdBy());
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

        var existing = iTestRepo.findByUserNameAndTime(payload.createdBy(),payload.startTime(),payload.endTime());
        if(!existing.isEmpty()){
            errors.add("Test already exists for the given user in the given time frame.");
        }

        CommonUtil.validateMandatory(payload.createdBy(), "Created By", errors);
        CommonUtil.validateMandatory(payload.name(), "Name", errors);
        CommonUtil.validateMandatory(payload.description(), "Description", errors);
        if(payload.totalMarks()==null){
            errors.add("Total Marks is mandatory.");
        }

        if(payload.questionBankId()==null || payload.questionBankId()<=0){
            errors.add("Question Bank Id is mandatory.");
        }else{
            // check if the question bank exist
            var questIonBank = iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId());
            if(questIonBank.isEmpty()){
                errors.add("Question Bank does not exist");
            }
            if(questIonBank.isPresent()){
                if(!questIonBank.get().getCreatedBy().equals(payload.createdBy())){
                    errors.add("User not authorized to add question in this question bank");
                }
            }
        }

        if(payload.startTime() == null) {
            errors.add("Start Time is mandatory.");
        }
        if(payload.endTime() == null) {
            errors.add("End Time is mandatory.");
        }
        if (payload.duration()== null) {
            errors.add("Duration is mandatory.");
        }

        if(payload.startTime() != null && payload.endTime() != null){

            LocalDateTime current = CommonUtil.getCurrentDateTime();
            if(current.isAfter(payload.startTime())||current.isEqual(payload.startTime())){
                errors.add("Start Time should be in the future.");
            }else if(current.isAfter(payload.endTime())||current.isEqual(payload.endTime())){
                errors.add("End Time should be in the future.");
            }else  if(payload.startTime().isAfter(payload.endTime())) {
                errors.add("Start Time should be before End Time.");
            }else if(payload.startTime().isEqual(payload.endTime())){
                errors.add("Start Time and End Time cannot be the same.");
            }
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }
}
