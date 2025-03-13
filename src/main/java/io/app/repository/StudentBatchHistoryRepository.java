package io.app.repository;

import io.app.model.StudentBatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentBatchHistoryRepository extends JpaRepository<StudentBatchHistory,Long> {
}
