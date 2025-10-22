package com.spxam.test_service.controller;

import com.spxam.test_service.dto.dashboard.DashBoardRes;
import com.spxam.test_service.exception.UserNotFoundException;
import com.spxam.test_service.service.IDashboardService;
import com.spxam.test_service.util.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;
    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardRes> getDashboard(@RequestParam("userName") String userName) {

        if(CommonUtil.isEmptyString(userName)){
            throw new UserNotFoundException("User must not be empty.");
        }
        DashBoardRes res = dashboardService.getDashboardDate(userName);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
