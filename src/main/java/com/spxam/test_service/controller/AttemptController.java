package com.spxam.test_service.controller;

import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.StartTestDto;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.service.IDoAttemptService;
import com.spxam.test_service.validator.attempt.DoAttemptValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AttemptController {

    private final IDoAttemptService iDoAttemptService;
    private final DoAttemptValidator doAttemptValidator;

    @PostMapping("/start/test")
    public ResponseEntity<String> startTest(StartTestDto payload){
        var validationMsg = doAttemptValidator.validateStartTest(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }

        iDoAttemptService.startTest(payload);

        return ResponseEntity.ok("Test started");
    }

    @PostMapping("/attempt/mcq")
    public ResponseEntity<String> attemptMcq(DoAttemptPayload payload){

        var validationMsg = doAttemptValidator.validate(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }

        iDoAttemptService.attemptMcq(payload);
        return ResponseEntity.ok("Attempted");
    }

    @PostMapping("/finish/test")
    public ResponseEntity<String> finishTest(StartTestDto payload){
        var validationMsg = doAttemptValidator.validateStartTest(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }
        iDoAttemptService.finishTest(payload);
        return ResponseEntity.ok("Test Finished");
    }
}
