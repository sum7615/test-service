package com.spxam.test_service.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

public class CountScoreEvent extends ApplicationEvent {
    private Long attemptId;

    public CountScoreEvent(Object source,Long attemptId) {
        super(source);
        this.attemptId = attemptId;
    }

    public Long getAttemptId() {
        return attemptId;
    }
}
