package com.spxam.test_service.service;

import com.spxam.test_service.dto.adminoperation.AssignTestPayload;

public interface IAdminActionService {
    void assignTestToUsers(AssignTestPayload payload);
    void revokeTestToUsers(AssignTestPayload payload);
}
