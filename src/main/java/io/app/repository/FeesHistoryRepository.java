package io.app.repository;

import io.app.model.FeesHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeesHistoryRepository extends JpaRepository<FeesHistory,Long> {
}
