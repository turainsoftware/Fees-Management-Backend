package io.app.repository;

import io.app.model.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeesRepository extends JpaRepository<Fees,Long> {
    @Query("SELECT f FROM Fees f WHERE f.student.id=:studentId AND f.batch.id=:batchId")
    public Optional<Fees> findByStudentIdAndBatchId(@Param("studentId") long studentId,@Param("batchId") long batchId);
}
