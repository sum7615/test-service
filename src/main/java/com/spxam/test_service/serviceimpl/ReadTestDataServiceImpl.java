package com.spxam.test_service.serviceimpl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.spxam.test_service.clients.IUserFeign;
import com.spxam.test_service.converter.TestToDtoConverter;
import com.spxam.test_service.dto.UserDt;
import com.spxam.test_service.dto.test.FetchTestAdmin;
import com.spxam.test_service.dto.test.TestRes;
import com.spxam.test_service.entity.TestUserMap;
import com.spxam.test_service.exception.NoTestFoundException;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.repo.ITestRepo;
import com.spxam.test_service.repo.ITestUserMapRep;
import com.spxam.test_service.service.IReadTestDataService;

import lombok.AllArgsConstructor;

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

		List<TestUserMap> res = iTestUserMapRep.getTestByUserName(userName)
				.orElseThrow(() -> new NoTestFoundException("No Data Found"));
		return TestToDtoConverter.convertToTestResList(res);

	}

	@Override
	public List<FetchTestAdmin> getTestAdmin(String userName) {
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
			throw new NoTestFoundException("No Data Found");
		}
		var res = iTestRepo.findByCreatedByIgnoreCase(userName);

		return TestToDtoConverter.convertTestToAdminResList(res);

	}
}
