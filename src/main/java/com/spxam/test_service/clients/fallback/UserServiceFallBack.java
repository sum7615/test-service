package com.spxam.test_service.clients.fallback;

import com.spxam.test_service.dto.UserDt;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserServiceFallBack {
    public UserDt getUserByUserName(String userName) {
        return new UserDt("unknown", "Unknown User", Set.of(), Set.of(), Set.of(), Set.of());
    }
}
