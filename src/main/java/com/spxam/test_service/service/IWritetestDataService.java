package com.spxam.test_service.service;

import com.spxam.test_service.dto.test.CreateTestPayload;
import com.spxam.test_service.dto.test.DeleteTestPayload;
import com.spxam.test_service.dto.test.UpdateTestPayload;

public interface IWritetestDataService {
    Long createTestData(CreateTestPayload payload);

	void updateTestData(UpdateTestPayload payload);

	void deleteTestData(DeleteTestPayload payload);
}
