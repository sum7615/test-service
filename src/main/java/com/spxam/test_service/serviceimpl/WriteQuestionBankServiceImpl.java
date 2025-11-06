package com.spxam.test_service.serviceimpl;

import org.springframework.stereotype.Service;

import com.spxam.test_service.dto.questionbanl.CreateQuestIonBankPayLoad;
import com.spxam.test_service.dto.questionbanl.DeleteQuestionBankPayload;
import com.spxam.test_service.dto.questionbanl.UpdateQuestionBankPayload;
import com.spxam.test_service.entity.QuestionBank;
import com.spxam.test_service.exception.InternalServerError;
import com.spxam.test_service.exception.InvalidRequestData;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.service.IWriteQuestionBankService;
import com.spxam.test_service.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WriteQuestionBankServiceImpl implements IWriteQuestionBankService {

	private final IQuestionBankRepo iQuestionBankRepo;
	private final ITestRepo iTestRepo;

	@Override
	public void deleteQuestionBank(DeleteQuestionBankPayload payLoad) {

		var qsnbank = iQuestionBankRepo.findByIdAndIsActiveTrue(payLoad.id());
		if (qsnbank.isEmpty()) {
			throw new InvalidRequestData("Bad request.");
		}
		var alltest = iTestRepo.findByQuestionBank(qsnbank.get().getId());
		if (!alltest.isEmpty()) {
			throw new NotValidRequest("Test associated");
		}
		var bank = qsnbank.get();

		bank.setUpdatedAt(CommonUtil.getCurrentDateTime());
		bank.setUpdatedBy(payLoad.userName());
		bank.setIsActive(false);

		try {
			iQuestionBankRepo.save(bank);
		} catch (Exception e) {
			throw new InternalServerError("Internal Server error");
		}

	}

	@Override
	public Long createQuestionBank(CreateQuestIonBankPayLoad payLoad) {

		QuestionBank questionBank = QuestionBank.builder().name(payLoad.name()).questionType(payLoad.questionType())
				.description(payLoad.description()).createdBy(payLoad.createdBy())
				.createdAt(CommonUtil.getCurrentDateTime()).isActive(true).build();
		questionBank= iQuestionBankRepo.save(questionBank);
		return questionBank.getId();
		
	}

	@Override
	public void updateQuestionBank(UpdateQuestionBankPayload payLoad) {

		var qsnbank = iQuestionBankRepo.findByIdAndIsActiveTrue(payLoad.id());
		if (qsnbank.isEmpty()) {
			throw new InvalidRequestData("Bad request.");
		}
		var alltest = iTestRepo.findByQuestionBank(qsnbank.get().getId());
		var current = CommonUtil.getCurrentDateTime();
		if (!alltest.isEmpty()) {
			alltest = alltest.filter(e -> !e.getStartTime().isAfter(current) && !e.getEndTime().isBefore(current));
			if (!alltest.isEmpty()) {
				throw new NotValidRequest("Test started");
			}
		}
		var bank = qsnbank.get();

		bank.setUpdatedAt(current);
		bank.setUpdatedBy(payLoad.userName());
		bank.setName(payLoad.name());
		bank.setDescription(payLoad.description());
		bank.setQuestionType(payLoad.type());

		try {
			iQuestionBankRepo.save(bank);
		} catch (Exception e) {
			throw new InternalServerError("Internal Server error");
		}

	}

}
