package com.spxam.test_service.validator.adminoperation;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.dto.adminoperation.AssignTestPayload;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.ValidationResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class AdminOperationValidator {
    private final IUserFeign iUserFeign;

    public ValidationResult validate(AssignTestPayload payload){
        List<String> errors = new ArrayList<>();
        CommonUtil.validateMandatory(payload.assignTo(),"Assign to",errors);
        CommonUtil.validateMandatory(payload.assignBy(),"Assigned by",errors);
        CommonUtil.validateMandatory(payload.testId(),"Test id",errors);
        // check assign by is valid and have proper role
        UserDt userDt = null;
        try {
            userDt = iUserFeign.getUserByUserName(payload.assignBy());
            if (userDt == null) {
                throw new UserNotFoundException("Assignee User Not Found");
            }
        } catch (Exception e) {
            throw new UserNotFoundException("Assignee User Not Found");
        }
        Set<String> roles = userDt.roles().stream().map(String::toLowerCase)
                .collect(java.util.stream.Collectors.toSet());
        if (roles.isEmpty()) {
            throw new UserNotFoundException("No Roles Found for Assignee User");
        }

        if (roles.contains("user")) {
            errors.add("Unauthorized Access");
        }
        userDt=null;

        // check assign to is valid and not having same test unattempted
        try {
            userDt = iUserFeign.getUserByUserName(payload.assignTo());
            if (userDt == null) {
                throw new UserNotFoundException("Assign to User Not Found");
            }
        } catch (Exception e) {
            throw new UserNotFoundException("Assign to User Not Found");
        }
        return ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .build();

    }
}
