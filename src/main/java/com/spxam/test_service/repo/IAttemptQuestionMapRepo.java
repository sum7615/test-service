package com.spxam.test_service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spxam.test_service.entity.AttemptQuestionMap;

public interface IAttemptQuestionMapRepo extends JpaRepository<AttemptQuestionMap, Long> {
	@Query("SELECT a FROM AttemptQuestionMap a WHERE a.attempt.id = :attemptId and a.isAttempted=false")
	List<AttemptQuestionMap> findByAtempt(@Param("attemptId")Long attemptId);
	
	@Query("SELECT a FROM AttemptQuestionMap a WHERE a.attempt.id = :attemptId and a.question.id = :questionId ")
	Optional<AttemptQuestionMap> findByAttemptAndQuestion(@Param("attemptId")Long attemptId, @Param("questionId") Long questionId);
	
	@Query("SELECT max(a.orderNo) from AttemptQuestionMap a WHERE a.attempt.id = :attemptId")
	Long getMaxOfOrder(@Param("attemptId") Long attemptId);
	
}
