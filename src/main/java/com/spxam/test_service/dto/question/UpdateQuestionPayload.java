package com.spxam.test_service.dto.question;

public record UpdateQuestionPayload(
		Long id,
		String title,
        String problemStatement,
        String problemStatementImg,
        String o1,
        String o2,
        String o3,
        String o4,
        String o5,
        String ans,
        Long mark,
        String type,
        String userName,
        Long questionBankId,
        String level) {

}
