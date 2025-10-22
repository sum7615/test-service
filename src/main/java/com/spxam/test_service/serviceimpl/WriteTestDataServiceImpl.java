package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.dto.test.CreateTestPayload;
import com.spxam.test_service.entity.Test;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.service.IWritetestDataService;
import com.spxam.test_service.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WriteTestDataServiceImpl implements IWritetestDataService {

	private final IQuestionBankRepo iQuestionBankRepo;
	private final ITestRepo iTestRepo;

	@Override
	public void createTestData(CreateTestPayload payload) {
		Test test = Test.builder().name(payload.name()).description(payload.description())
				.startTime(payload.startTime()).endTime(payload.endTime()).duration(payload.duration())
				.createdBy(payload.createdBy()).createdDate(CommonUtil.getCurrentDateTime()).isActive(true)
				.totalMark(payload.totalMarks()).build();
		if (payload.questionBankId() != null) {
			iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId()).ifPresent(test::setQuestionBank);
		}
		iTestRepo.save(test);
		return;
	}
}
