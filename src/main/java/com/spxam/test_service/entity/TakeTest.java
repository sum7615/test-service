package com.spxam.test_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="take_test")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class TakeTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="")
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="attempt_id",referencedColumnName = "id")
    private Attempt attemptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id",referencedColumnName = "id")
    private Question question;

    @Column(name="rsp")
    private String rsp;

    @Column(name="is_correct")
    private Boolean isCorrect;

    @Column(name="answered_at")
    private LocalDateTime answeredAt;
}
