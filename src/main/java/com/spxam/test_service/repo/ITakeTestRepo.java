package com.spxam.test_service.repo;

import com.spxam.test_service.entity.TakeTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITakeTestRepo extends JpaRepository<TakeTest,Long> {
}
