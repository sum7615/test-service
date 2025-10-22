package com.spxam.test_service.service;

import com.spxam.test_service.dto.test.TestRes;

import java.util.List;

public interface IReadTestDataService {
    List<TestRes> getTestByUserName(String userName);
}
