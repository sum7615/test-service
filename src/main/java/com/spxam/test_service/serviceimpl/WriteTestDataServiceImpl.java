package com.spxam.test_service.serviceimpl;

import org.springframework.stereotype.Service;

import com.spxam.test_service.dto.test.CreateTestPayload;
import com.spxam.test_service.dto.test.DeleteTestPayload;
import com.spxam.test_service.dto.test.UpdateTestPayload;
import com.spxam.test_service.entity.Level;
import com.spxam.test_service.entity.Test;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.repo.ILevelRepo;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.service.IWritetestDataService;
import com.spxam.test_service.util.CommonUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WriteTestDataServiceImpl implements IWritetestDataService {

	private final IQuestionBankRepo iQuestionBankRepo;
	private final ITestRepo iTestRepo;
	private final ILevelRepo iTestLevelRepo;
	@Override
	@Transactional
	public Long createTestData(CreateTestPayload payload) {

		String testLvl = CommonUtil.isEmptyString(payload.testLevel()) ? "Easy" : payload.testLevel();
		Level testLevel =  iTestLevelRepo.getLevelByLevelName(testLvl)
					.orElseThrow(()->new NotValidRequest("Not Valid level"));
		
		Test test = Test.builder()
				.name(payload.name())
				.description(payload.description())
				.startTime(payload.startTime())
				.endTime(payload.endTime())
				.duration(payload.duration())
				.createdBy(payload.createdBy())
				.createdDate(CommonUtil.getCurrentDateTime())
				.isActive(true)
				.totalMark(payload.totalMarks())
				.passMark(payload.passMark())
				.level(testLevel)
				.totalQuestions(payload.totalQuestions())
				.build();
		if (payload.questionBankId() != null) {
			iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId()).ifPresent(test::setQuestionBank);
		}
		test=iTestRepo.save(test);
		return test.getId();
	}
	@Override
	@Transactional
	public void updateTestData(UpdateTestPayload payload) {
		String testLvl = CommonUtil.isEmptyString(payload.testLevel()) ? "Easy" : payload.testLevel();
		Level testLevel =  iTestLevelRepo.getLevelByLevelName(testLvl)
					.orElse(null);
		
		if(testLevel==null) {
			throw new NotValidRequest("Not Valid level");
		}
		
		var test=iTestRepo.findById(payload.id()).orElseThrow( ()-> new NotValidRequest("No test"));
		
		test.setUpdatedDate(CommonUtil.getCurrentDateTime());
		test.setUpdatedBy(payload.userName());
		test.setDescription(payload.description());
		test.setDuration(payload.duration());
		test.setEndTime(payload.endTime());
		test.setIsActive(payload.isActive());
		test.setLevel(testLevel);
		test.setName(payload.name());
		test.setPassMark(payload.passMark());
		test.setStartTime(payload.startTime());
		test.setTotalMark(payload.totalMarks());
		test.setTotalQuestions(payload.totalQuestions());
		
		if (payload.questionBankId() != null) {
			iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId()).ifPresent(test::setQuestionBank);
		}
		iTestRepo.save(test);
	}
	@Override
	public void deleteTestData(DeleteTestPayload payload) {

		var test=iTestRepo.findById(payload.id()).orElseThrow( ()-> new NotValidRequest("No test"));
		var current = CommonUtil.getCurrentDateTime();
		// check if the test is running
		if(test.getStartTime().isAfter(current)|| current.isBefore(current)) {
			throw new NotValidRequest("Ongoing");
		}
		
		
		
		test.setUpdatedBy(payload.userName());
		test.setUpdatedDate(current);
		test.setIsActive(false);
		iTestRepo.save(test);
	}
	
	
}
