package com.spxam.test_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_user_map")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class TestUserMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test testId;

    @Column(name = "user_name")
    private String userName;
}
