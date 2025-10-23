package com.spxam.test_service.event;

import com.spxam.test_service.entity.TakeTest;
import com.spxam.test_service.repo.IAttemptRepo;
import com.spxam.test_service.repo.ITakeTestRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CountScoreConsumer implements ApplicationListener<CountScoreEvent> {
    private final IAttemptRepo iAttemptRepo;
    private final ITakeTestRepo iTakeTestRepo;

    @Override
    public void onApplicationEvent(CountScoreEvent event) {
        var attempt = iAttemptRepo.findById(event.getAttemptId()).orElse(null);
        long score = 0L;
        if(attempt!=null){
            List<TakeTest> allQuestionAttempted= iTakeTestRepo.findByAttemptId(event.getAttemptId());
            if(!allQuestionAttempted.isEmpty()){
                score = allQuestionAttempted.stream()
                        .filter(TakeTest::getIsCorrect)
                        .mapToLong(e -> e.getQuestion().getMarks()).sum();
            }
            attempt.setScore(score);
            iAttemptRepo.save(attempt);
        }
    }
}
