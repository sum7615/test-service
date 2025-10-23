package com.spxam.test_service.repo;

import com.spxam.test_service.entity.TestUserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITestUserMapRep extends JpaRepository<TestUserMap,Long> {
    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName) and t.isActive=true and t.isCompleted=false and t.testId.is = :testId and t.isStarted = true and t.testId.endTime > CURRENT_TIMESTAMP")
    List<TestUserMap> getOngoingByUserAndTest(@Param("userName") String userName, @Param("testId") Long testId);
    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName) and t.isActive=true and t.isCompleted=false and t.isStarted = true and t.testId.endTime > CURRENT_TIMESTAMP")
    List<TestUserMap> getOngoingTest(@Param("userName") String userName);

    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName)")
    Optional<List<TestUserMap>> getTestByUserName(@Param("userName") String userName);

    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName) and t.testId.endTime > CURRENT_TIMESTAMP and t.testId.startTime > CURRENT_TIMESTAMP ")
    List<TestUserMap> getFutureTestByUserName(@Param("userName") String userName);

    @Query("Select t from TestUserMap t where lower(t.assignedTo)=lower( :userName) and t.testId.id= :testId and t.isActive=true and t.isCompleted=false and t.isStarted=false order by assignedAt desc")
    List<TestUserMap> getByUserAndTestId(@Param("userName") String userName, @Param("testId") Long testId);


}
