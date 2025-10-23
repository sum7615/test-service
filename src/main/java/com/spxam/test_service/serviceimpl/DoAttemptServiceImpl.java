package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.StartTestDto;
import com.spxam.test_service.entity.*;
import com.spxam.test_service.event.CountScoreEvent;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.repo.*;
import com.spxam.test_service.service.IDoAttemptService;
import com.spxam.test_service.util.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoAttemptServiceImpl implements IDoAttemptService {

    private final ITestUserMapRep iTestUserMapRep;
    private final IQuestionRepo iQuestionRepo;
    private final ITakeTestRepo iTakeTestRepo;
    private final IAttemptRepo iAttemptRepo;
    private final ITestRepo iTestRepo;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    @Transactional
    public void finishTest(StartTestDto payload) {

        List<TestUserMap> toFinish = iTestUserMapRep.getByUserAndTestId(payload.userName(),payload.testId());
        if (toFinish.isEmpty()){
            throw new NoTestFoundException("No test found for user to finish.");
        }
        var testMap = toFinish.getFirst();
        if(!testMap.getIsStarted()){
            throw new NoTestFoundException("Test not started for user.");
        }
        if(testMap.getIsCompleted()){
            throw new NoTestFoundException("Test already finished for user.");
        }

        var ongoingAttempts = iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(),payload.testId());
        if(ongoingAttempts.isEmpty()){
            throw new NoTestFoundException("No ongoing attempt found for user to finish.");
        }
        var attempt = ongoingAttempts.getFirst();
        attempt.setFinishedAt(CommonUtil.getCurrentDateTime());

        testMap.setIsCompleted(true);

        try {
            iTestUserMapRep.save(testMap);
            iAttemptRepo.save(attempt);
            applicationEventPublisher.publishEvent(new CountScoreEvent(this,attempt.getId()));
        }catch (Exception e){
            throw new NoTestFoundException("Error finishing test for user.");
        }

    }

    @Override
    public void startTest(StartTestDto payload) {
        List<TestUserMap> toStart = iTestUserMapRep.getByUserAndTestId(payload.userName(),payload.testId());
        if (toStart.isEmpty()){
            throw new NoTestFoundException("No test found for user to start.");
        }
        if(!iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(),payload.testId()).isEmpty()){
            throw new NoTestFoundException("Test already started for user.");
        }
        var test = iTestRepo.findById(payload.testId());
        if(test.isEmpty()){
            throw new NoTestFoundException("Test not found.");
        }

        if(!Objects.equals(toStart.getFirst().getTestId().getId(), payload.testId())){
            throw new NoTestFoundException("No test found for user to start.");
        }

        var testMap = toStart.getFirst();
        testMap.setIsStarted(true);
        testMap.setIsCompleted(false);


        Attempt attempt = Attempt.builder()
                .userName(payload.userName())
                .attemptedAt(CommonUtil.getCurrentDateTime())
                .testId(test.get())
                .build();
        try {
            iTestUserMapRep.save(testMap);
            iAttemptRepo.save(attempt);
        }catch (Exception e){
            throw new NoTestFoundException("Error starting test for user.");
        }
    }

    @Override
    public void attemptMcq(DoAttemptPayload payload) {

        // check user are allowed to attempt the test

        // check if the user has already attempted the test

        // check if the user has started the test
        List<TestUserMap> ongoingTests = iTestUserMapRep.getOngoingTest(payload.userName());

        if(ongoingTests.isEmpty()){
            throw new NoTestFoundException("No ongoing test found for user.");
        }

        TestUserMap test =ongoingTests.stream().filter(testUserMap -> testUserMap.getTestId().getId().equals(payload.testId()))
                .findFirst().orElseThrow(() -> new NoTestFoundException("No ongoing test found for user with given test."));

       var aalAttemptForThisTest= iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(),payload.testId());

       if(aalAttemptForThisTest.isEmpty()) {
           throw new NoTestFoundException("No ongoing attempt found for user with given test.");
       }

        Optional<Question> question = iQuestionRepo.findById(payload.questionId());

        if(question.isEmpty()){
            throw new NoTestFoundException("Question not found.");
        }

        // check the question is part of the test assigned to the user
        if(!Objects.equals(question.get().getBankId().getId(), test.getTestId().getQuestionBank().getId())){
            throw new NoTestFoundException("Question not part of the test assigned to the user.");
        }
        TakeTest takenTest=null;

        takenTest= iTakeTestRepo.findByQuestAndAttempt(question.get().getId(),aalAttemptForThisTest.getFirst().getId()).orElse(null);
        var current = CommonUtil.getCurrentDateTime();
        if(takenTest!=null) {
            // update the existing attempt
            takenTest.setLastUpdatedAt(current);
            takenTest.setRsp(payload.ans());
            takenTest.setTotalAttemptCount(takenTest.getTotalAttemptCount()+1);
            takenTest.setTimeTakenInSeconds(payload.timeTakenInSeconds());
            takenTest.setIsCorrect(payload.ans().equalsIgnoreCase(question.get().getAns()));
        }else {
            takenTest = TakeTest.builder()
                    .attemptId(aalAttemptForThisTest.getFirst())
                    .answeredAt(current)
                    .question(question.get())
                    .rsp(payload.ans())
                    .totalAttemptCount(1L)
                    .timeTakenInSeconds(payload.timeTakenInSeconds())
                    .lastUpdatedAt(current)
                    .isCorrect(payload.ans().equalsIgnoreCase(question.get().getAns()))
                    .build();
        }
        iTakeTestRepo.save(takenTest);
    }

}
