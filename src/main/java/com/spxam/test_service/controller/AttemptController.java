package com.spxam.test_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spxam.test_service.dto.attempt.AttemptQstRes;
import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.ResumeTestRes;
import com.spxam.test_service.dto.attempt.StartTestDto;
import com.spxam.test_service.dto.attempt.ViewTestPayload;
import com.spxam.test_service.dto.attempt.ViewTestRes;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.service.IDoAttemptService;
import com.spxam.test_service.validator.attempt.DoAttemptValidator;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AttemptController {

    private final IDoAttemptService iDoAttemptService;
    private final DoAttemptValidator doAttemptValidator;

    @PostMapping("/resume/test")
    public ResponseEntity<List<ResumeTestRes>>resumeTest(@RequestBody ViewTestPayload payload){
    	 var validationMsg = doAttemptValidator.validate(payload);
         
         if(!validationMsg.isValid()){
             throw new NotValidRequest(validationMsg.getErrors().toString());
         }
         
         
         List<ResumeTestRes> res = iDoAttemptService.resumeTest(payload);
         
         return ResponseEntity.ok(res);
         
    }
    
    @PostMapping("view/test")
    public ResponseEntity<ViewTestRes> getTestData(@RequestBody ViewTestPayload payload){
        var validationMsg = doAttemptValidator.validate(payload);
        
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }

        ViewTestRes res = iDoAttemptService.getTestData(payload);
        
        return ResponseEntity.ok(res);

    }
    
    @PostMapping("/start/test")
    public ResponseEntity<String> startTest(@RequestBody StartTestDto payload){
        var validationMsg = doAttemptValidator.validateStartTest(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }

        iDoAttemptService.startTest(payload);

        return ResponseEntity.ok("Test started");
    }

    @PostMapping("/attempt/mcq")
    public ResponseEntity<String> attemptMcq(@RequestBody DoAttemptPayload payload){

        var validationMsg = doAttemptValidator.validate(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }
        iDoAttemptService.attemptMcq(payload);
        return ResponseEntity.ok("Attempted");
    }

    @PostMapping("/finish/test")
    public ResponseEntity<String> finishTest(@RequestBody StartTestDto payload){
        var validationMsg = doAttemptValidator.validateStartTest(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }
        iDoAttemptService.finishTest(payload);
        return ResponseEntity.ok("Test Finished");
    }

    @PostMapping("/get/qns")
    public ResponseEntity<AttemptQstRes> getQuestion(@RequestBody StartTestDto payload){
        var validationMsg = doAttemptValidator.validateStartTest(payload);
        if(!validationMsg.isValid()){
            throw new NotValidRequest(validationMsg.getErrors().toString());
        }
        AttemptQstRes res =iDoAttemptService.getQuestion(payload);
        return ResponseEntity.ok(res);
    }
}
