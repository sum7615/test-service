package com.spxam.test_service.repo;

import com.spxam.test_service.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAttemptRepo extends JpaRepository<Attempt,Long> {
    List<Attempt> findByUserName(String userName);
}
