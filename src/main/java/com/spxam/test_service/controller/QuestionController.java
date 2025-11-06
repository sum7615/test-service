package com.spxam.test_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spxam.test_service.dto.question.CreateQuestionPayload;
import com.spxam.test_service.dto.question.DeleteQuestionPayload;
import com.spxam.test_service.dto.question.QuestionDataRes;
import com.spxam.test_service.dto.question.UpdateQuestionPayload;
import com.spxam.test_service.service.IReadQuestionService;
import com.spxam.test_service.service.IWriteQuestionService;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.question.QuestionControllerValidator;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/question")
public class QuestionController {

	private final IReadQuestionService iReadQuestionService;
	private final IWriteQuestionService iWriteQuestionService;
	private final QuestionControllerValidator questionControllerValidator;

	@GetMapping("/fetch")
	public ResponseEntity<List<QuestionDataRes>> fetchQuestions(@RequestParam("userName") String userName,
			@RequestParam("questionBankId") Long questionBankId) {
		if (CommonUtil.isEmptyString(userName)) {
			return ResponseEntity.badRequest().build();
		} else if (questionBankId == null || questionBankId <= 0) {
			return ResponseEntity.badRequest().build();
		}

		List<QuestionDataRes> res = iReadQuestionService.getQuestions(userName, questionBankId);

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PostMapping("/create")
	public ResponseEntity<String> createQuestion(@RequestBody CreateQuestionPayload payload) {

		var validationMsg = questionControllerValidator.validate(payload);
		if (!validationMsg.isValid()) {
			return new ResponseEntity<>(String.join("; ", validationMsg.getErrors()), HttpStatus.BAD_REQUEST);
		}

		Long id = iWriteQuestionService.createQuestion(payload);
		
		
		return ResponseEntity
                .ok("{\"id\": "+id+"}"); 
	}
	
	// update question
	@PutMapping("/update")
	public ResponseEntity<String> updateQuestion(@RequestBody UpdateQuestionPayload payload){
		var validationMsg = questionControllerValidator.validateUpdate(payload);
		if (!validationMsg.isValid()) {
			return new ResponseEntity<>(String.join("; ", validationMsg.getErrors()), HttpStatus.BAD_REQUEST);
		}
		iWriteQuestionService.updateQuestion(payload);

		return ResponseEntity
	                 .ok("{\"status\": \"Question updated.\"}");    
		
	}
	
	// remove question
	
	@PostMapping("/delete")
	
	public ResponseEntity<String>deleteQuestion(@RequestBody DeleteQuestionPayload payload){
		var validationMsg = questionControllerValidator.validateDelete(payload);
		if (!validationMsg.isValid()) {
			return new ResponseEntity<>(String.join("; ", validationMsg.getErrors()), HttpStatus.BAD_REQUEST);
		}
		iWriteQuestionService.deleteQuestion(payload);

		return ResponseEntity
	                 .ok("{\"status\": \"Question deleted.\"}");   
		
	}
	
	
	
	
	
}
