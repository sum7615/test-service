package com.spxam.test_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "level")
@NoArgsConstructor
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;
    
    @Column(name="value")
    private Long value;

    @Column(name="description")
    private String description;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

}
