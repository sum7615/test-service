package com.spxam.test_service.repo;

import com.spxam.test_service.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITestRepo extends JpaRepository<Test,Long> {


    @Query("SELECT t FROM Test t WHERE t.questionBank.id = :questionBankId AND t.isActive = true")
    Optional<Test> findByQuestionBank(@Param("questionBankId") Long questionBankId);

    List<Test> findByCreatedByIgnoreCase(@Param("createdBy") String createdBy);

    @Query("SELECT t FROM Test t WHERE lower(t.createdBy) =lower( :userName) AND t.startTime = :startTime AND t.endTime = :endTime")
    List<Test> findByUserNameAndTime(@Param("userName") String userName, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
}
