package com.spxam.test_service.repo;

import com.spxam.test_service.entity.TestUserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITestUserMapRep extends JpaRepository<TestUserMap,Long> {

    @Query("Select t from TestUserMap t where lower(t.userName)=lower( :userName)")
    Optional<List<TestUserMap>> getTestByUserName(@Param("userName") String userName);
}
