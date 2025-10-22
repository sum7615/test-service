package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.converter.QuestionBankToDtoConverter;
import com.spxam.test_service.dto.questionbanl.QuestionBankLookUpRes;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.exception.NotAuthorizedAccess;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.repo.IQuestionBankRepo;
import com.spxam.test_service.service.IReadQuestionBankService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReadQuestionBankServiceImpl implements IReadQuestionBankService {

	private final IUserFeign iUserFeign;
	private final IQuestionBankRepo iQuestionBankRepo;
	private final QuestionBankToDtoConverter questionBankToDtoConverter;

	@Override
	public List<QuestionBankLookUpRes> fetchQuestionBanks(String userName) {
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
		} else if (roles.contains("user")) {
			throw new NotAuthorizedAccess("Unauthorized Access");
		}

		var entityObjs = iQuestionBankRepo.findByCreatedByAndIsActiveTrue(userName);

		return questionBankToDtoConverter.toDtoList(entityObjs);
	}
}
