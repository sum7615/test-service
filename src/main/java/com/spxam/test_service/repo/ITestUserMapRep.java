package com.spxam.test_service.repo;

import com.spxam.test_service.entity.TestUserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITestUserMapRep extends JpaRepository<TestUserMap,Long> {

    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName)")
    Optional<List<TestUserMap>> getTestByUserName(@Param("userName") String userName);

    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName) and t.testId.endTime > CURRENT_TIMESTAMP and t.testId.startTime > CURRENT_TIMESTAMP ")
    List<TestUserMap> getFutureTestByUserName(@Param("userName") String userName);


}
