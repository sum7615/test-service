package com.spxam.test_service.converter;

import com.spxam.test_service.dto.test.TestRes;
import com.spxam.test_service.entity.Test;
import com.spxam.test_service.entity.TestUserMap;

import java.util.List;

public class TestToDtoConverter {

    public static List<TestRes> convertToTestResList(List<TestUserMap> data) {
        return data.stream().map(dt -> {
            var test = dt.getTestId();
            return new TestRes(test.getId(), test.getName(), test.getDescription());
        }).toList();
    }

    public static List<TestRes> convertTestToTestResList(List<Test> data) {
        return data.stream().map(test -> {
            return new TestRes(test.getId(), test.getName(), test.getDescription());
        }).toList();
    }
}
