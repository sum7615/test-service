package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.dto.adminoperation.AssignTestPayload;
import com.spxam.test_service.entity.Test;
import com.spxam.test_service.entity.TestUserMap;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.repo.ITestUserMapRep;
import com.spxam.test_service.service.IAdminActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminActionServiceImpl implements IAdminActionService {
    private final ITestRepo iTestRepo;
    private final ITestUserMapRep iTestUserMapRep;

    @Override
    public void assignTestToUsers(AssignTestPayload payload) {

        // check test is available

        Test test = iTestRepo.findById(payload.testId()).orElseThrow(()
                -> new NoTestFoundException("Test not found."));


        // check if users are already having same unattempted test

       var userTest= iTestUserMapRep.getFutureTestByUserName(payload.assignTo());
       if(!userTest.isEmpty()){
           throw new NoTestFoundException("User(s) already have unattempted test assigned.");
       }

        // assign test to users

        iTestUserMapRep.save(TestUserMap.builder()
                .assignedTo(payload.assignTo())
                .assignedBy(payload.assignBy())
                .testId(test)
                .build());

    }
}
