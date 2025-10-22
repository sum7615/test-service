package com.spxam.test_service.repo;

import com.spxam.test_service.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IQuestionBankRepo extends JpaRepository<QuestionBank,Long> {

    List<QuestionBank> findByCreatedByAndIsActiveTrue(@Param("createdBy") String createdBy);

    @Query("SELECT qb FROM QuestionBank qb WHERE qb.createdBy = :createdBy AND qb.createdAt = :createdAt AND qb.isActive = true")
    List<QuestionBank> findByUserAndTime(@Param("createdBy") String createdBy, @Param("createdAt") LocalDateTime createdAt);

    Optional<QuestionBank> findByIdAndIsActiveTrue(@Param("id") Long id);
}
