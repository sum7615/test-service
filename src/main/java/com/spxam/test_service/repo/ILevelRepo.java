package com.spxam.test_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spxam.test_service.entity.Level;

public interface ILevelRepo extends JpaRepository<Level,Long> {

    @Query("SELECT tl FROM Level tl WHERE lower(tl.name) = lower(:levelName)")
    Optional<Level> getLevelByLevelName(@Param("levelName") String levelName);

}
