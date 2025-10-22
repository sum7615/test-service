package com.spxam.test_service.service;

import com.spxam.test_service.dto.dashboard.DashBoardRes;

public interface IDashboardService {
    DashBoardRes getDashboardDate(String userName);
}
