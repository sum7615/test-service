package com.spxam.test_service.entity;

import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="question_bank")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuestionBank {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="id")
   private Long  id;

    @Column(name="name")
    private String name;

    @Column(name="created_at")
   private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="question_qnty")
    private Long questionQnty;

    @Column(name="question_type")
   private String questionType;

    @Column(name="is_active")
    private Boolean isActive;

}
