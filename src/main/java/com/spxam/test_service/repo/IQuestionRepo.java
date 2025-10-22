package com.spxam.test_service.repo;

import com.spxam.test_service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IQuestionRepo extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.bankId.id = :questionBankId AND lower(q.createdBy) = lower( :createdBy) AND q.isActive = true")
    List<Question> findByQuestionBankId(@Param("questionBankId") Long questionBankId, @Param("createdBy") String createdBy);

    @Query("SELECT SUM(q.marks) FROM Question q WHERE q.bankId.id = :questionBankId AND q.isActive = true")
    Long sumOfMarksByQuestionBankId(@Param("questionBankId") Long questionBankId);

    @Query("SELECT q FROM Question q WHERE lower(q.title) = lower(:title) AND q.bankId.id = :questionBankId AND lower(q.createdBy) = lower(:createdBy) AND (lower(q.problemStatement) = lower(:problemStatement) OR lower(q.problemStatementImg) = lower(:problemStatementImg) )AND q.isActive = true")
    List<Question> checkIfQuestionExist(@Param("title") String title, @Param("questionBankId") Long questionBankId,@Param("createdBy") String createdBy,@Param("problemStatement") String problemStatement, @Param("problemStatementImg")String problemStatementImg);


}
