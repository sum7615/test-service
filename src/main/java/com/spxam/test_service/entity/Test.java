package com.spxam.test_service.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="test")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

	@Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "revoked_date")
    private LocalDateTime revokedDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "created_by")
    private String  createdBy;

    @Column(name = "updated_by")
    private String  updatedBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_bank_id", referencedColumnName = "id")
    private QuestionBank questionBank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Level level;

    @Column(name = "total_mark")
    private Long totalMark;

    @Column(name = "total_questions")
    private Long totalQuestions;

    @Column(name = "pass_mark")
    private Long passMark;

    @Column(name = "is_active")
    private Boolean isActive;
}
