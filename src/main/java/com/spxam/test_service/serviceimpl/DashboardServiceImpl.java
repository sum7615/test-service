package com.spxam.test_service.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.dto.dashboard.DashBoardRes;
import com.spxam.test_service.dto.dashboard.PastExam;
import com.spxam.test_service.dto.dashboard.UpcomingExam;
import com.spxam.test_service.entity.Attempt;
import com.spxam.test_service.entity.Test;
import com.spxam.test_service.entity.TestUserMap;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.repo.IAttemptRepo;
import com.spxam.test_service.repo.ITakeTestRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.repo.ITestUserMapRep;
import com.spxam.test_service.service.IDashboardService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

	private final IUserFeign iUserFeign;
	private final ITestRepo iTestRepo;
	private final ITestUserMapRep iTestUserMapRep;
	private final IAttemptRepo iAttemptRepo;
	private final ITakeTestRepo iTakeTestRepo;

	@Override
	public DashBoardRes getDashboardDate(String userName) {
		UserDt userDt = null;
		try {
			userDt = iUserFeign.getUserByUserName(userName);
			if (userDt == null) {
				throw new UserNotFoundException("User Not Found");
			}
		} catch (Exception e) {
			throw new UserNotFoundException("User Not Found");
		}
		Set<String> roles = userDt.roles().stream().map(String::toLowerCase)
				.collect(java.util.stream.Collectors.toSet());
		if (roles.isEmpty()) {
			throw new UserNotFoundException("No Roles Found for User");
		}
		var userTestMap = iTestUserMapRep.getTestByUserName(userName);

		Set<Test> allTest = userTestMap.get().stream().map(TestUserMap::getTestId).collect(Collectors.toSet());

//		allTest.addAll(iTestRepo.findByCreatedByIgnoreCase(userName));

		List<Attempt> allAttempt = iAttemptRepo.findByUserName(userName);

		List<Long> completedTest =allAttempt.stream().filter(e->e.getFinishedAt()!=null).map(e->e.getTestId().getId()).toList(); 
		List<Test> attemptedTest = allAttempt.stream().filter(t->t.getFinishedAt()!=null).map(Attempt::getTestId).toList();

		allTest.addAll(attemptedTest);

		List<UpcomingExam> upComingExam = new ArrayList<>();
		List<PastExam> pastExam = new ArrayList<>();

		LocalDateTime currentDateTime = LocalDateTime.now();

		// Up-coming test
		allTest.stream().filter(e -> currentDateTime.isBefore(e.getEndTime())&&!completedTest.contains(e.getId()))
		.forEach(at -> {
			UpcomingExam ut = UpcomingExam.builder().id(at.getId()).title(at.getName()).description(at.getDescription())
					.startTime(at.getStartTime()).endTime(at.getEndTime()).totalMark(at.getTotalMark())
					.passMark(at.getPassMark()).duration(at.getDuration()).status("Pending").build();
			upComingExam.add(ut);
		});

		// past test
		Map<Long, Attempt> attemptMap = allAttempt.stream()
			    .collect(Collectors.toMap(a -> a.getTestId().getId(), Function.identity()));

		allTest.stream()
	    .filter(e -> currentDateTime.isAfter(e.getEndTime()) || completedTest.contains(e.getId()))
	    .forEach(e -> {
	    	Attempt attempt = attemptMap.get(e.getId());

	        Long attemptedDuration = 0L;
	        Long questionAttempted = 0L;
	        Long obtainedMark = 0L;
	        String status = "Expired";
	        LocalDateTime attemptStartTime = null;
	        LocalDateTime attemptEndTime = null;

	        if (attempt != null && attemptedTest.contains(e)) {
	            if (attempt.getAttemptedAt() != null && attempt.getFinishedAt() != null) {
	                attemptedDuration = Duration.between(attempt.getAttemptedAt(), attempt.getFinishedAt()).toSeconds();
	            }

	            var answeredQuestions = iTakeTestRepo.findByAttemptId(attempt.getId());
	            if (answeredQuestions != null && !answeredQuestions.isEmpty()) {
	                questionAttempted = answeredQuestions.stream()
	                        .map(tt -> tt.getQuestion().getId())
	                        .distinct()
	                        .count();
	            }

	            obtainedMark = attempt.getScore();
	            status = "Attempted";
	            attemptStartTime = attempt.getAttemptedAt();
	            attemptEndTime = attempt.getFinishedAt();
	        }

	        PastExam pt = PastExam.builder()
	            .id(e.getId())
	            .title(e.getName())
	            .description(e.getDescription())
	            .startTime(e.getStartTime())
	            .endTime(e.getEndTime())
	            .totalMark(e.getTotalMark())
	            .passMark(e.getPassMark())
	            .duration(e.getDuration())
	            .attemptStartTime(attemptStartTime)
	            .attemptEndTime(attemptEndTime)
	            .obtainedMark(obtainedMark)
	            .attemptedDuration(attemptedDuration)
	            .totalQsnAttempted(questionAttempted)
	            .status(status)
	            .build();

	        pastExam.add(pt);
	    });

		return DashBoardRes.builder().past(pastExam).upcoming(upComingExam).build();

	}
}
