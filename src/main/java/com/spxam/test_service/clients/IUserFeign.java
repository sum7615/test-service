package com.spxam.test_service.clients;

import com.spxam.test_service.clients.fallback.UserServiceFallBack;
import com.spxam.test_service.dto.UserDt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",fallback = UserServiceFallBack.class)
public interface IUserFeign {
    @GetMapping("/user/prfl")
    public UserDt getUserByUserName(@RequestParam("userName") String userName);

}
