package com.spxam.test_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="attempt")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name ="user_name")
   private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="test_id",referencedColumnName = "id")
    private Test testId;

    @Column(name ="attempted_at")
    private LocalDateTime  attemptedAt;

    @Column(name ="finished_at")
    private LocalDateTime finishedAt;

    @Column(name ="score")
    private Long score;
}
