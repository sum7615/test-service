package com.spxam.test_service.dto.question;

import lombok.Builder;

@Builder
public record CreateQuestionPayload(String title,
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
                                    String createdBy,
                                    Long questionBankId,
                                    String level
                                    ) {
}
