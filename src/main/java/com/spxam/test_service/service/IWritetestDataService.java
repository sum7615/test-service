package com.spxam.test_service.service;

import com.spxam.test_service.dto.test.CreateTestPayload;

public interface IWritetestDataService {
    void createTestData(CreateTestPayload payload);
}
