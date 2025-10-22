package com.spxam.test_service.repo;

import com.spxam.test_service.entity.TakeTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITakeTestRepo extends JpaRepository<TakeTest,Long> {

    @Query("SELECT tt FROM TakeTest tt WHERE tt.attemptId.id = :attemptId")
    public List<TakeTest> findByAttemptId(@Param("attemptId") Long attemptId);
}
