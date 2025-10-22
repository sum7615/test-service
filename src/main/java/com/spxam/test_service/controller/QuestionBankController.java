package com.spxam.test_service.controller;

import com.spxam.test_service.dto.questionbanl.CreateQuestIonBankPayLoad;
import com.spxam.test_service.dto.questionbanl.QuestionBankLookUpRes;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.service.IReadQuestionBankService;
import com.spxam.test_service.service.IWriteQuestionBankService;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.questionbank.QuestionBankControllerVallidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class QuestionBankController {
    private final IReadQuestionBankService iReadQuestionBankService;
    private final QuestionBankControllerVallidator questionBankControllerVallidator;
    private final IWriteQuestionBankService iWriteQuestionBankService;

    @GetMapping("/question-banks")
    public ResponseEntity<List<QuestionBankLookUpRes>> getQuestionBanks(@RequestParam("userName")String userName ) {
        if(CommonUtil.isEmptyString(userName)){
            throw new NotValidRequest("UserName cannot be empty");
        }
        List<QuestionBankLookUpRes> res =  iReadQuestionBankService.fetchQuestionBanks(userName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("create-question-bank" )
    public ResponseEntity<String> createQuestionBank(@RequestBody CreateQuestIonBankPayLoad payLoad){

        var exceptionMsg = questionBankControllerVallidator.validateCreateQuestionBankPayload(payLoad);
        if(!exceptionMsg.isValid()){
            throw new NotValidRequest(String.join(", ",exceptionMsg.getErrors()));
        }

        iWriteQuestionBankService.createQuestionBank(payLoad);
        return new ResponseEntity<>("Question Bank created.",HttpStatus.OK);
    }

}
