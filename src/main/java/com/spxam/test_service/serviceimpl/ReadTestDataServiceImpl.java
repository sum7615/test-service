package com.spxam.test_service.serviceimpl;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.dto.test.TestRes;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.entity.TestUserMap;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.repo.ITestUserMapRep;
import com.spxam.test_service.service.IReadTestDataService;
import com.spxam.test_service.converter.TestToDtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReadTestDataServiceImpl implements IReadTestDataService {
	private final ITestUserMapRep iTestUserMapRep;

	private final IUserFeign iUserFeign;
	private final ITestRepo iTestRepo;

	@Override
	public List<TestRes> getTestByUserName(String userName) {

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
		if (roles.contains("user")) {
			List<TestUserMap> res = iTestUserMapRep.getTestByUserName(userName)
					.orElseThrow(() -> new NoTestFoundException("No Data Found"));
			return TestToDtoConverter.convertToTestResList(res);
		} else {
			var res = iTestRepo.findByCreatedByIgnoreCase(userName);
			return TestToDtoConverter.convertTestToTestResList(res);
		}
	}
}
