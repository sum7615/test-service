package com.spxam.test_service.controller;

import com.spxam.test_service.dto.test.CreateTestPayload;
import com.spxam.test_service.dto.test.DeleteTestPayload;
import com.spxam.test_service.dto.test.FetchTestAdmin;
import com.spxam.test_service.dto.test.TestRes;
import com.spxam.test_service.dto.test.UpdateTestPayload;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.service.IReadTestDataService;
import com.spxam.test_service.service.IWritetestDataService;
import com.spxam.test_service.util.CommonUtil;
import com.spxam.test_service.validator.test.TestControllerValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final IReadTestDataService iReadTestDataService;
    private final IWritetestDataService iWritetestDataService;
    private final TestControllerValidator testControllerValidator;

    @GetMapping("/fetch/{userName}")
    public ResponseEntity<List<TestRes>> getTest(@PathVariable("userName") String userName,
                                                 @RequestParam(name = "role",required=false,defaultValue = "") String role
                                           ) {
        if(CommonUtil.isEmptyString(userName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        var res = iReadTestDataService.getTestByUserName(userName);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    @GetMapping("/admin/{userName}")
    public ResponseEntity<List<FetchTestAdmin>> getTestAdmin(@PathVariable("userName") String userName                                ) {
        if(CommonUtil.isEmptyString(userName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        var res = iReadTestDataService.getTestAdmin(userName);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTest(@RequestBody CreateTestPayload payload) {
        var validationMsg = testControllerValidator.vaidationCreateTestPayload(payload);

        if(!validationMsg.isValid()) {
           throw new NotValidRequest(validationMsg.getErrors().toString());
        }
       var id= iWritetestDataService.createTestData(payload);
		return ResponseEntity
                .ok("{\"id\": "+id+"}");     }

    // update test
    
    @PutMapping("update")
    public ResponseEntity<String> update(@RequestBody UpdateTestPayload payload){
    	  var validationMsg = testControllerValidator.vaidationUpdateTestPayload(payload);

          if(!validationMsg.isValid()) {
             throw new NotValidRequest(validationMsg.getErrors().toString());
          }
          iWritetestDataService.updateTestData(payload);
          
      	return ResponseEntity
                .ok("{\"status\": \"Test updated.\"}");    
    	
    }
    
    // delete test
    @PostMapping("delete")
    public ResponseEntity<String>delete(@RequestBody DeleteTestPayload payload){
    	 var validationMsg = testControllerValidator.vaidationDeleteTestPayload(payload);

         if(!validationMsg.isValid()) {
            throw new NotValidRequest(validationMsg.getErrors().toString());
         }
         iWritetestDataService.deleteTestData(payload);
         
     	return ResponseEntity
               .ok("{\"status\": \"Test deleted.\"}");   
    }
    
    
    
}
