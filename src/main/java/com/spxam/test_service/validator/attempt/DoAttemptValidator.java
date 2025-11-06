package com.spxam.test_service.validator.attempt;

import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.StartTestDto;
import com.spxam.test_service.dto.attempt.ViewTestPayload;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DoAttemptValidator {
	
	public ValidationResult validate(ViewTestPayload payload) {
	       List<String> errors = new ArrayList<>();
	        CommonUtil.validateMandatory(payload.userName(),"User Name",errors);
	        CommonUtil.validateMandatory(payload.testId(),"Test",errors);
	       
	        return new ValidationResult(errors.isEmpty(), errors);

	}
	
	
    public ValidationResult validate(DoAttemptPayload payload) {
       List<String> errors = new ArrayList<>();
        CommonUtil.validateMandatory(payload.userName(),"User Name",errors);
        CommonUtil.validateMandatory(payload.questionId(),"Question",errors);
        CommonUtil.validateMandatory(payload.testId(),"Test",errors);
        CommonUtil.validateMandatory(payload.ans(),"Ans",errors);
        return new ValidationResult(errors.isEmpty(), errors);
    }

    public ValidationResult validateStartTest(StartTestDto payload) {
       List<String> errors = new ArrayList<>();
        CommonUtil.validateMandatory(payload.userName(),"User Name",errors);
        CommonUtil.validateMandatory(payload.testId(),"Test",errors);
        return new ValidationResult(errors.isEmpty(), errors);
    }


}
