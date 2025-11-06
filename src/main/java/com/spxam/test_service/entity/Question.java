package com.spxam.test_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name="question")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="problem_statement")
    private String problemStatement;

    @Column(name="problem_statement_img")
    private String problemStatementImg;

    @Column(name="o_1")
    private String o1;

    @Column(name="o_2")
    private String o2;

    @Column(name="o_3")
    private String o3;

    @Column(name="o_4")
    private String o4;

    @Column(name="o_5")
    private String o5;

    @Column(name="ans")
    private String ans;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "mark")
    private Long marks;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "type")
    private String type;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bank_id",referencedColumnName = "id")
    private QuestionBank bankId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Level level;
}
