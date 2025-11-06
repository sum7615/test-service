package com.spxam.test_service.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.spxam.test_service.dto.attempt.AttemptQstRes;
import com.spxam.test_service.dto.attempt.DoAttemptPayload;
import com.spxam.test_service.dto.attempt.ResumeTestRes;
import com.spxam.test_service.dto.attempt.StartTestDto;
import com.spxam.test_service.dto.attempt.ViewTestPayload;
import com.spxam.test_service.dto.attempt.ViewTestRes;
import com.spxam.test_service.entity.Attempt;
import com.spxam.test_service.entity.AttemptQuestionMap;
import com.spxam.test_service.entity.Question;
import com.spxam.test_service.entity.TakeTest;
import com.spxam.test_service.entity.Test;
import com.spxam.test_service.entity.TestUserMap;
import com.spxam.test_service.event.CountScoreEvent;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.repo.IAttemptQuestionMapRepo;
import com.spxam.test_service.repo.IAttemptRepo;
import com.spxam.test_service.repo.IQuestionRepo;
import com.spxam.test_service.repo.ITakeTestRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.repo.ITestUserMapRep;
import com.spxam.test_service.service.IDoAttemptService;
import com.spxam.test_service.util.CommonUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DoAttemptServiceImpl implements IDoAttemptService {

	private final ITestUserMapRep iTestUserMapRep;
	private final IQuestionRepo iQuestionRepo;
	private final ITakeTestRepo iTakeTestRepo;
	private final IAttemptRepo iAttemptRepo;
	private final ITestRepo iTestRepo;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final IAttemptQuestionMapRepo attemptQuestionMapRepo;

	@Override
	public List<ResumeTestRes> resumeTest(ViewTestPayload payload) {

		// check if user has the test
		List<TestUserMap> toFinish = iTestUserMapRep.getByUserAndTest(payload.userName(), payload.testId());
		if (toFinish.isEmpty()) {
			throw new NoTestFoundException("No test found.");
		}

		// check if this is already started
		var testMap = toFinish.getFirst();
		if (!testMap.getIsStarted()) {
			throw new NoTestFoundException("Test not started for user.");
		}

		var ongoingAttempts = iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(), payload.testId());
		if (ongoingAttempts.isEmpty()) {
			throw new NoTestFoundException("No ongoing attempt found for user to finish.");
		}

		var attempt = ongoingAttempts.getFirst();

		if (!attempt.getUserName().equalsIgnoreCase(testMap.getAssignedTo())
				|| !Objects.equals(attempt.getTestId().getId(), payload.testId())
				|| !Objects.equals(attempt.getTestId().getId(), testMap.getTestId().getId())) {
			throw new NoTestFoundException("No ongoing attempt found for user.");
		}

		// check if the duration is

		var allQuestAttempted = iTakeTestRepo.findByAttemptId(attempt.getId());
		List<ResumeTestRes> res = allQuestAttempted.stream().map(e -> {
			var qsn = e.getQuestion();
			return ResumeTestRes.builder().attemptedAns(e.getRsp()).marks(qsn.getMarks()).o1(qsn.getO1())
					.o2(qsn.getO2()).o3(qsn.getO3()).o4(qsn.getO4()).o5(qsn.getO5())
					.problemStatement(qsn.getProblemStatement()).problemStatementImg(qsn.getProblemStatementImg())
					.title(qsn.getTitle()).id(qsn.getId()).build();
		}).toList();
		
		if(res.isEmpty()) {
			List<AttemptQuestionMap> futureQuestion = attemptQuestionMapRepo.findByAtempt(attempt.getId());

			if (futureQuestion.isEmpty()) {
				return null;
			}

			Collections.shuffle(futureQuestion);

			Question exactQsn = futureQuestion.getLast().getQuestion();
			
			ResumeTestRes t = ResumeTestRes.builder()
			.attemptedAns(null)
			.marks(exactQsn.getMarks())
			.problemStatement(exactQsn.getProblemStatement())
			.problemStatementImg(exactQsn.getProblemStatementImg())
			.title(exactQsn.getTitle())
			.type(exactQsn.getType())
			.o1(exactQsn.getO1())
			.o2(exactQsn.getO2())
			.o3(exactQsn.getO3())
			.o4(exactQsn.getO4())
			.o5(exactQsn.getO5())
			.id(exactQsn.getId())
			.build();
			
			return List.of(t);
			
		}else {
			return res;
		}
	}

	@Override
	public ViewTestRes getTestData(ViewTestPayload payload) {

		// check if user has the test
		List<TestUserMap> toFinish = iTestUserMapRep.getByUserAndTest(payload.userName(), payload.testId());
		if (toFinish.isEmpty()) {
			throw new NoTestFoundException("No test found.");
		}

		// check if this is already started
		var testMap = toFinish.getFirst();
//        if(testMap.getIsStarted()){
//            throw new NoTestFoundException("Test started for user.");
//        }
		if (testMap.getIsCompleted()) {
			throw new NoTestFoundException("Test already finished for user.");
		}

		Test test = testMap.getTestId();

		return ViewTestRes.builder().id(test.getId()).assignedAt(testMap.getAssignedAt()).name(test.getName())
				.description(test.getDescription()).duration(test.getDuration()).startTime(test.getStartTime())
				.endTime(test.getEndTime()).totalMark(test.getTotalMark()).totalQuestions(test.getTotalQuestions())
				.passMark(test.getPassMark()).isStarted(testMap.getIsStarted()).build();
	}

	@Override
	public AttemptQstRes getQuestion(StartTestDto payload) {

		List<TestUserMap> toFinish = iTestUserMapRep.getOngoingByUserAndTest(payload.userName(), payload.testId());
		if (toFinish.isEmpty()) {
			throw new NoTestFoundException("No test found for user to finish.");
		}
		var testMap = toFinish.getFirst();
		if (!testMap.getIsStarted()) {
			throw new NoTestFoundException("Test not started for user.");
		}
		if (testMap.getIsCompleted()) {
			throw new NoTestFoundException("Test already finished for user.");
		}

		var ongoingAttempts = iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(), payload.testId());
		if (ongoingAttempts.isEmpty()) {
			throw new NoTestFoundException("No ongoing attempt found for user to finish.");
		}

		var attempt = ongoingAttempts.getFirst();

		if (!attempt.getUserName().equalsIgnoreCase(testMap.getAssignedTo())
				|| !Objects.equals(attempt.getTestId().getId(), payload.testId())
				|| !Objects.equals(attempt.getTestId().getId(), testMap.getTestId().getId())) {
			throw new NoTestFoundException("No ongoing attempt found for user.");
		}

		List<AttemptQuestionMap> futureQuestion = attemptQuestionMapRepo.findByAtempt(attempt.getId());

		if (futureQuestion.isEmpty()) {
			return null;
		}

		Collections.shuffle(futureQuestion);

		Question exactQsn = futureQuestion.getLast().getQuestion();

		return AttemptQstRes.builder().marks(exactQsn.getMarks()).o1(exactQsn.getO1()).o2(exactQsn.getO2())
				.o3(exactQsn.getO3()).o4(exactQsn.getO4()).o5(exactQsn.getO5())
				.problemStatement(exactQsn.getProblemStatement()).problemStatementImg(exactQsn.getProblemStatementImg())
				.title(exactQsn.getTitle()).type(exactQsn.getType()).id(exactQsn.getId()).build();
	}

	@Override
	@Transactional
	public void finishTest(StartTestDto payload) {

		List<TestUserMap> toFinish = iTestUserMapRep.getOngoingByUserAndTest(payload.userName(), payload.testId());
		if (toFinish.isEmpty()) {
			throw new NoTestFoundException("No test found for user to finish.");
		}
		var testMap = toFinish.getFirst();
		if (!testMap.getIsStarted()) {
			throw new NoTestFoundException("Test not started for user.");
		}
		if (testMap.getIsCompleted()) {
			throw new NoTestFoundException("Test already finished for user.");
		}

		var ongoingAttempts = iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(), payload.testId());
		if (ongoingAttempts.isEmpty()) {
			throw new NoTestFoundException("No ongoing attempt found for user to finish.");
		}
		var attempt = ongoingAttempts.getFirst();
		attempt.setFinishedAt(CommonUtil.getCurrentDateTime());

		testMap.setIsCompleted(true);

		try {
			iTestUserMapRep.save(testMap);
			iAttemptRepo.save(attempt);
			applicationEventPublisher.publishEvent(new CountScoreEvent(this, attempt.getId()));
		} catch (Exception e) {
			throw new NoTestFoundException("Error finishing test for user.");
		}

	}

	@Override
	@Transactional
	public void startTest(StartTestDto payload) {
		List<TestUserMap> toStart = iTestUserMapRep.getByUserAndTestId(payload.userName(), payload.testId());
		if (toStart.isEmpty()) {
			throw new NoTestFoundException("No test found for user to start.");
		}
		if (!iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(), payload.testId()).isEmpty()) {
			throw new NoTestFoundException("Test already started for user.");
		}
		var test = iTestRepo.findById(payload.testId());
		if (test.isEmpty()) {
			throw new NoTestFoundException("Test not found.");
		}

		if (!Objects.equals(toStart.getFirst().getTestId().getId(), payload.testId())) {
			throw new NoTestFoundException("No test found for user to start.");
		}

		var testMap = toStart.getFirst();
		testMap.setIsStarted(true);
		testMap.setIsCompleted(false);

		Attempt attempt = Attempt.builder().userName(payload.userName()).attemptedAt(CommonUtil.getCurrentDateTime())
				.testId(test.get()).build();
		try {
			final Attempt attemptSaved = iAttemptRepo.save(attempt);

			// select questions at put into the AttemptQuestionMap

			List<AttemptQuestionMap> attemptQuestionMaps = new ArrayList<>(
					Math.toIntExact(test.get().getTotalQuestions()));

			List<Question> allQuestions = iQuestionRepo
					.findByQuestionBank(testMap.getTestId().getQuestionBank().getId());

			final LocalDateTime current = CommonUtil.getCurrentDateTime();

			if (allQuestions.size() == testMap.getTestId().getTotalQuestions()) {

				var totalAvailavleMark = allQuestions.stream().mapToLong(Question::getMarks).sum();
				var totalmarks = testMap.getTestId().getTotalMark();
				if (totalAvailavleMark == totalmarks) {
					// Randomize the order of questions
					Collections.shuffle(allQuestions);

					attemptQuestionMaps = allQuestions.stream()
							.map(q -> AttemptQuestionMap.builder().attempt(attemptSaved).createdAt(current)
									.isAttempted(false).question(q).mark(q.getMarks()).build())
							.collect(Collectors.toList());
				} else {
					throw new NotValidRequest("Validation error. Please contact admin.");
				}
			} else if (allQuestions.size() < testMap.getTestId().getTotalQuestions()) {
				throw new NotValidRequest("Validation error. Please contact admin.");
			} else {

				List<Question> selectedQuestions = getQuestionForTest(allQuestions, testMap.getTestId().getTotalMark(),
						testMap.getTestId().getTotalQuestions(), testMap.getTestId().getLevel().getName());
				if (selectedQuestions.size() != testMap.getTestId().getTotalQuestions()) {
					throw new NotValidRequest("Validation error. Please contact admin.");
				}

				attemptQuestionMaps = selectedQuestions.stream()
						.map(q -> AttemptQuestionMap.builder().attempt(attemptSaved).createdAt(current)
								.isAttempted(false).question(q).mark(q.getMarks()).build())
						.collect(Collectors.toList());

			}

			iTestUserMapRep.save(testMap);
			attemptQuestionMapRepo.saveAll(attemptQuestionMaps);
		} catch (NotValidRequest n) {
			throw new NotValidRequest(n.getMessage());
		} catch (Exception e) {
			throw new NoTestFoundException("Error starting test for user.");
		}
	}

	@Override
	public void attemptMcq(DoAttemptPayload payload) {

		// check user are allowed to attempt the test

		// check if the user has already attempted the test

		// check if the user has started the test
		
		List<TestUserMap> ongoingTests = iTestUserMapRep.getOngoingTest(payload.userName());

		if (ongoingTests.isEmpty()) {
			throw new NoTestFoundException("No ongoing test found for user.");
		}

		TestUserMap test = ongoingTests.stream()
				.filter(testUserMap -> testUserMap.getTestId().getId().equals(payload.testId())).findFirst()
				.orElseThrow(() -> new NoTestFoundException("No ongoing test found for user with given test."));

		var aalAttemptForThisTest = iAttemptRepo.findOngoingAttemptByUserAndTest(payload.userName(), payload.testId());

		if (aalAttemptForThisTest.isEmpty()) {
			throw new NoTestFoundException("No ongoing attempt found for user with given test.");
		}

		Optional<Question> question = iQuestionRepo.findById(payload.questionId());

		if (question.isEmpty()) {
			throw new NoTestFoundException("Question not found.");
		}

		// check the question is part of the test assigned to the user
		if (!Objects.equals(question.get().getBankId().getId(), test.getTestId().getQuestionBank().getId())) {
			throw new NoTestFoundException("Question not part of the test assigned to the user.");
		}
		var current = CommonUtil.getCurrentDateTime();
		var durationInSeconds = test.getTestId().getDuration();

		if (current.isBefore(aalAttemptForThisTest.getFirst().getAttemptedAt())) {
			throw new NotValidRequest("Start the test first.");
		} else {
			// check if the test duration is not exceeded
			var timeElapsedInSeconds = java.time.Duration
					.between(aalAttemptForThisTest.getFirst().getAttemptedAt(), current).getSeconds();
			if (timeElapsedInSeconds > durationInSeconds) {
				finishTest(new StartTestDto(payload.userName(), payload.testId()));
				throw new NoTestFoundException("Test duration exceeded.");
			}
		}
		
		
		// check if the question is from the attemptQuestionMap questions
		
		var attemptQuestionMap =  attemptQuestionMapRepo.findByAttemptAndQuestion(aalAttemptForThisTest.getFirst().getId(),question.get().getId());
		if(attemptQuestionMap.isEmpty()) {
			throw new NoTestFoundException("Who are you?");
		}else {
			
			
			var qstAttmp = attemptQuestionMap.get();
			qstAttmp.setIsAttempted(true);
			
			if(qstAttmp.getOrderNo()==null ||qstAttmp.getOrderNo()<1 ) {
				Long maxOrder = attemptQuestionMapRepo.getMaxOfOrder(aalAttemptForThisTest.getFirst().getId());
				if(maxOrder==null) {
					maxOrder=0L;
				}
				qstAttmp.setOrderNo(maxOrder+1);
			}
			
			qstAttmp.setUpdatedAt(current);
			attemptQuestionMapRepo.save(qstAttmp);
		}
		
		TakeTest takenTest = null;

		takenTest = iTakeTestRepo
				.findByQuestAndAttempt(question.get().getId(), aalAttemptForThisTest.getFirst().getId()).orElse(null);
		if (takenTest != null) {
			// update the existing attempt
			takenTest.setLastUpdatedAt(current);
			takenTest.setRsp(payload.ans());
			takenTest.setTotalAttemptCount(takenTest.getTotalAttemptCount() + 1);
			takenTest.setTimeTakenInSeconds(payload.timeTakenInSeconds());
			takenTest.setIsCorrect(payload.ans().equalsIgnoreCase(question.get().getAns()));
		} else {
			takenTest = TakeTest.builder().attemptId(aalAttemptForThisTest.getFirst()).answeredAt(current)
					.question(question.get()).rsp(payload.ans()).totalAttemptCount(1L)
					.timeTakenInSeconds(payload.timeTakenInSeconds()).lastUpdatedAt(current)
					.isCorrect(payload.ans().equalsIgnoreCase(question.get().getAns())).build();
		}
		iTakeTestRepo.save(takenTest);
	}

	private List<Question> getQuestionForTest(List<Question> allQuestions, Long totalMark, Long totalQuestion,
			String testLevel) {
		if (allQuestions == null || allQuestions.isEmpty() || totalQuestion == null || totalMark == null
				|| testLevel == null) {
			return Collections.emptyList();
		}

		// Step 1: Split questions by level
		List<Question> matchingLevelQuestions = allQuestions.stream()
				.filter(q -> q.getLevel() != null && testLevel.equalsIgnoreCase(q.getLevel().getName()))
				.collect(Collectors.toList());

		List<Question> otherLevelQuestions = allQuestions.stream()
				.filter(q -> q.getLevel() == null || !testLevel.equalsIgnoreCase(q.getLevel().getName()))
				.collect(Collectors.toList());

		// Step 2: Shuffle for randomization
		Collections.shuffle(matchingLevelQuestions);
		Collections.shuffle(otherLevelQuestions);

		// Step 3: Greedily select half from matching and half from others
		long halfCount = totalQuestion / 2;
		long remainingCount = totalQuestion - halfCount;

		List<Question> preSelected = new ArrayList<>();
		preSelected.addAll(matchingLevelQuestions.stream().limit(halfCount).collect(Collectors.toList()));
		preSelected.addAll(otherLevelQuestions.stream().limit(remainingCount).collect(Collectors.toList()));

		// Step 4: If total marks don’t match, use DP to optimize selection
		long currentMarks = preSelected.stream().mapToLong(Question::getMarks).sum();

		if (currentMarks == totalMark) {
			return preSelected;
		}

		// Combine all into candidate pool
		List<Question> candidatePool = new ArrayList<>(allQuestions);
		Collections.shuffle(candidatePool);

		return optimizeByDP(candidatePool, totalMark.intValue(), totalQuestion.intValue());
	}

	private List<Question> optimizeByDP(List<Question> questions, int totalMark, int totalQuestion) {
		int n = questions.size();
		long[][] dp = new long[totalQuestion + 1][totalMark + 1];
		boolean[][][] take = new boolean[n][totalQuestion + 1][totalMark + 1];

		// DP build
		for (int i = 0; i < n; i++) {
			int mark = questions.get(i).getMarks().intValue();
			for (int qCount = totalQuestion; qCount >= 1; qCount--) {
				for (int m = totalMark; m >= mark; m--) {
					long without = dp[qCount][m];
					long with = dp[qCount - 1][m - mark] + mark;
					if (with > without) {
						dp[qCount][m] = with;
						take[i][qCount][m] = true;
					}
				}
			}
		}

		// Step 5: Reconstruct chosen questions
		List<Question> selected = new ArrayList<>();
		int m = totalMark;
		int qCount = totalQuestion;

		for (int i = n - 1; i >= 0 && qCount > 0; i--) {
			if (m >= questions.get(i).getMarks() && take[i][qCount][m]) {
				selected.add(questions.get(i));
				m -= questions.get(i).getMarks();
				qCount--;
			}
		}

		// If exact totalMark isn’t possible, select closest sum ≤ totalMark
		long achievedMarks = selected.stream().mapToLong(Question::getMarks).sum();
		if (achievedMarks < totalMark && selected.size() < totalQuestion) {
			// Try to fill remaining slots greedily with smallest-mark questions
			List<Question> remaining = new ArrayList<>(questions);
			remaining.removeAll(selected);
			remaining.sort(Comparator.comparingLong(Question::getMarks));
			for (Question q : remaining) {
				if (selected.size() < totalQuestion && achievedMarks + q.getMarks() <= totalMark) {
					selected.add(q);
					achievedMarks += q.getMarks();
				}
			}
		}

		return selected;
	}

}
