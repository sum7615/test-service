package com.spxam.test_service.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_bank_id", referencedColumnName = "id")
    private QuestionBank questionBank;

    @Column(name = "totalMark")
    private Long totalMark;

    @Column(name = "is_active")
    private Boolean isActive;
}
