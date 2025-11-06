package com.spxam.test_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "attempt_question_map")
@Entity
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AttemptQuestionMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id",referencedColumnName = "id")
    private Attempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",referencedColumnName = "id")
    private Question question;

    @Column(name = "order_no")
    private Long orderNo;

    @Column(name = "is_attempted")
    private Boolean isAttempted;

    @Column(name = "mark")
    private Long mark;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
