package com.spxam.test_service.serviceimpl;

import org.springframework.stereotype.Service;

import com.spxam.test_service.dto.question.CreateQuestionPayload;
import com.spxam.test_service.dto.question.DeleteQuestionPayload;
import com.spxam.test_service.dto.question.UpdateQuestionPayload;
import com.spxam.test_service.entity.Level;
import com.spxam.test_service.entity.Question;
import com.spxam.test_service.exception.NotValidRequest;
import com.spxam.test_service.repo.ILevelRepo;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.repo.IQuestionRepo;
import com.spxam.test_service.service.IWriteQuestionService;
import com.spxam.test_service.util.CommonUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WriteQuestionServiceImpl implements IWriteQuestionService {
	private final IQuestionRepo iQuestionRepo;
	private final IQuestionBankRepo iQuestionBankRepo;
	private final ILevelRepo levelRepo;

	@Transactional
	@Override
	public void deleteQuestion(DeleteQuestionPayload payload) {

		var question =iQuestionRepo.findById(payload.id()).orElseThrow(()->{
			throw new NotValidRequest("not valid request.");
		});

		question.setIsActive(false);
		question.setUpdatedAt(CommonUtil.getCurrentDateTime());
		iQuestionRepo.save(question);
	}
	
	@Transactional
	@Override
	public void updateQuestion(UpdateQuestionPayload payload) {
		
		var question = iQuestionRepo.findById(payload.id()).orElseThrow(()->{
			throw new NotValidRequest("Not a valid request");
		});
		
		if(!question.getBankId().getId().equals(payload.questionBankId())) {
			throw new NotValidRequest("Not a valid request");
		}
		Level level = levelRepo.getLevelByLevelName(payload.level()).orElseThrow(()->new NotValidRequest("Level is not valid"));

		
		question.setTitle(payload.title());
		question.setAns(payload.ans());
		question.setProblemStatement(payload.problemStatement());
		question.setProblemStatementImg(payload.problemStatementImg());
		question.setMarks(payload.mark());
		question.setType(payload.type());
		question.setUpdatedAt(CommonUtil.getCurrentDateTime());
		question.setUpdatedBy(payload.userName());
		question.setO1(payload.o1());
		question.setO2(payload.o2());
		question.setO3(payload.o3());
		question.setO4(payload.o4());
		question.setO5(payload.o5());
		question.setLevel(level);		
		iQuestionRepo.save(question);
		
	}
	@Transactional
	@Override
	public Long createQuestion(CreateQuestionPayload payload) {

		var questIonBank = iQuestionBankRepo.findByIdAndIsActiveTrue(payload.questionBankId()).orElse(null);

		Level level = levelRepo.getLevelByLevelName(payload.level()).orElseThrow(()->new NotValidRequest("Level is not valid"));
		
		Question qs = Question.builder().title(payload.title()).problemStatement(payload.problemStatement())
				.problemStatementImg(payload.problemStatementImg()).o1(payload.o1()).o2(payload.o2()).o3(payload.o3())
				.o4(payload.o4()).o5(payload.o5()).ans(payload.ans()).marks(payload.mark()).type(payload.type())
				.createdBy(payload.createdBy()).createdAt(CommonUtil.getCurrentDateTime()).bankId(questIonBank)
				.level(level)
				.isActive(true).build();
		qs = iQuestionRepo.save(qs);
		return qs.getId();
	}


}
