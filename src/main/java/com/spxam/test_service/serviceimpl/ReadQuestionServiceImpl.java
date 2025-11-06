package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.dto.question.QuestionDataRes;
import com.spxam.test_service.exception.NotAuthorizedAccess;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.repo.IQuestionRepo;
import com.spxam.test_service.service.IReadQuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service

public class ReadQuestionServiceImpl implements IReadQuestionService {

	private final IUserFeign iUserFeign;
	private final IQuestionRepo iQuestionRepo;

	@Override
	public List<QuestionDataRes> getQuestions(String userName, Long questionBankId) {
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
			throw new NotAuthorizedAccess("No Roles Found for User");
		}
		if (roles.contains("user")) {
			throw new NotAuthorizedAccess("Unauthorized Access");
		}

		return iQuestionRepo.findByQuestionBankId(questionBankId, userName).stream()
				.map(q -> QuestionDataRes.builder().id(q.getId()).title(q.getTitle())
						.problemStatement(q.getProblemStatement()).problemStatementImg(q.getProblemStatementImg())
						.o1(q.getO1()).o2(q.getO2()).o3(q.getO3()).o4(q.getO4()).o5(q.getO5()).ans(q.getAns()).type(q.getType())
						.mark(q.getMarks()).level(q.getLevel().getName()).build())
				.toList();

	}
}
