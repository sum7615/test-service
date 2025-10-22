package com.spxam.test_service.validator.question;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.dto.question.CreateQuestionPayload;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.IQuestionRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.ValidationResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class QuestionControllerValidator {

   private final IQuestionBankRepo iQuestionBankRepo;
   private final IQuestionRepo iQuestionRepo;
   private final IUserFeign iUserFeign;
   private final ITestRepo iTestRepo;
    public ValidationResult validate(CreateQuestionPayload payload){
        List<String> errors = new java.util.ArrayList<>();

        CommonUtil.validateMandatory(payload.title(),"Title",errors);

        if(CommonUtil.isEmptyString(payload.problemStatement())&& CommonUtil.isEmptyString(payload.problemStatementImg())){
            errors.add("Either Problem Statement or Problem Statement Img must be provided");
        }

        CommonUtil.validateMandatory(payload.o1(),"Option 1",errors);
        CommonUtil.validateMandatory(payload.o2(),"Option 2",errors);
        CommonUtil.validateMandatory(payload.o3(),"Option 3",errors);
        CommonUtil.validateMandatory(payload.o4(),"Option 4",errors);
        CommonUtil.validateMandatory(payload.ans(),"Ans",errors);
        CommonUtil.validateMandatory(payload.createdBy(),"Created By",errors);
        CommonUtil.validateMandatory(payload.type(),"Type",errors);

        if(payload.mark()==null||payload.mark()<=0){
            errors.add("Mark must be greater than 0");
        }
        if(payload.questionBankId()==null||payload.questionBankId()<=0){
            errors.add("Question Bank Id must not be empty");
        }

        // check if the question bank exist
        var questIonBank = iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId());
       if(questIonBank.isEmpty()){
           errors.add("Question Bank does not exist");
       }

        // check if the user have the authority to add question in the question bank
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

        if(questIonBank.isPresent()){
            if(!questIonBank.get().getCreatedBy().equals(payload.createdBy())){
                errors.add("User not authorized to add question in this question bank");
            }
        }

        // check the mark of the question with the test


        var testOptional = iTestRepo.findByQuestionBank(payload.questionBankId());
        if(testOptional.isPresent()){
            Long totalMarksInTest = iQuestionRepo.sumOfMarksByQuestionBankId(payload.questionBankId());
            Long testTotalMarks = testOptional.get().getTotalMark();
            if(totalMarksInTest==null){
                totalMarksInTest=0L;
            }
            if((totalMarksInTest + payload.mark()) > testTotalMarks){
                errors.add("Total marks of questions exceed the total marks of the test");
            }
        }else{
            errors.add("No test associated with the question bank");
        }


        // check the type of question

        if(questIonBank.isPresent()){
            String questionType = questIonBank.get().getQuestionType();
            if(!questionType.equalsIgnoreCase(payload.type())){
                errors.add("Question type does not match with the question bank type");
            }
        }

        // check if the same question exists

        var existingQuestion = iQuestionRepo.checkIfQuestionExist(
                payload.title(),
                payload.questionBankId(),
                payload.createdBy(),
                payload.problemStatement(),
                payload.problemStatementImg()
        );
        if(!existingQuestion.isEmpty()){
            errors.add("Same question already exists in the question bank");
        }
        return new ValidationResult(errors.isEmpty(),errors);
    }
}
