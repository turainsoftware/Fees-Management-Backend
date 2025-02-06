package io.app.repository;

import io.app.dto.BatchEndYearMonthProjection;
import io.app.model.Batch;
import io.app.model.Teacher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch,Long> {
    List<Batch> findByTeacher(Teacher teacher);
    boolean existsByName(String name);
    @Cacheable(value = "batches",key = "#batchId")
    public Optional<Batch> findById(Long id);

    public boolean existsByNameAndTeacher(String name,Teacher teacher);

    @Query("SELECT new io.app.dto.BatchEndYearMonthProjection(b.id,b.endYear,b.endMonth,b.monthlyFees) FROM Batch b WHERE b.id = :id")
    public Optional<BatchEndYearMonthProjection> findEndYearMonthById(@Param("id") Long id);

}
