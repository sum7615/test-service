package com.spxam.test_service.dto.attempt;

import lombok.Builder;

@Builder
public record ResumeTestRes(Long id,String title, String problemStatement, String problemStatementImg,
        String o1, String o2, String o3, String o4, String o5,String type,
        Long marks,String attemptedAns) {

}
