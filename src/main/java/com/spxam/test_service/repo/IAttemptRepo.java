package com.spxam.test_service.repo;

import com.spxam.test_service.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAttemptRepo extends JpaRepository<Attempt,Long> {
    List<Attempt> findByUserName(String userName);

    @Query("SELECT a FROM Attempt a WHERE lower(a.userName) = lower(:userName) AND a.testId.id = :testId AND a.finishedAt is null order by a.attemptedAt DESC")
    List<Attempt>findOngoingAttemptByUserAndTest(@Param("userName") String userName,@Param("testId") Long testId);
}
