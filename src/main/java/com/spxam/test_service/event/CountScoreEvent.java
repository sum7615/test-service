package com.spxam.test_service.event;

import org.springframework.context.ApplicationEvent;

public class CountScoreEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6154000840857257969L;
	private Long attemptId;

    public CountScoreEvent(Object source,Long attemptId) {
        super(source);
        this.attemptId = attemptId;
    }

    public Long getAttemptId() {
        return attemptId;
    }
}
