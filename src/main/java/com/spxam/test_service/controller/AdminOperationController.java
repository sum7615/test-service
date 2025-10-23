package com.spxam.test_service.controller;

import com.spxam.test_service.dto.adminoperation.AssignTestPayload;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.service.IAdminActionService;
import com.spxam.test_service.validator.adminoperation.AdminOperationValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminOperationController {

    private final IAdminActionService iAdminActionService;
    private final AdminOperationValidator adminOperationValidator;

    @PostMapping("assign/test")
    public ResponseEntity<String> assignTestToUsers(@RequestBody AssignTestPayload payload) {
        var validationMsg =  adminOperationValidator.validate(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }
       iAdminActionService.assignTestToUsers(payload);
       return new ResponseEntity<>("Test assigned successfully.", HttpStatus.OK);
    }

    @PostMapping("/revoke/test")
    public ResponseEntity<String> revokeTestToUsers(@RequestBody AssignTestPayload payload) {
        var validationMsg =  adminOperationValidator.validate(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }
        iAdminActionService.revokeTestToUsers(payload);
        return new ResponseEntity<>("Test revoked successfully.", HttpStatus.OK);
    }

}
