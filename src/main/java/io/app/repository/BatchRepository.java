package io.app.repository;

import io.app.dto.BatchEndYearMonthProjection;
import io.app.dto.Projections.BatchProjection;
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


    @Query("SELECT COUNT(b) FROM Batch b WHERE b.teacher.id = :teacherId AND MONTH(b.createdAt) = MONTH(CURRENT_DATE) AND YEAR(b.createdAt) = YEAR(CURRENT_DATE)")
    Long countBatchesByTeacherInCurrentMonth(Long teacherId);

    @Query("SELECT COUNT(b) FROM Batch b WHERE b.teacher.id = :teacherId")
    Long countBatchesByTeacherId(Long teacherId);

    @Query("SELECT COUNT(b) FROM Batch b WHERE b.teacher.id = :teacherId AND MONTH(b.createdAt) = MONTH(CURRENT_DATE - 1 MONTH) AND YEAR(b.createdAt) = YEAR(CURRENT_DATE - 1 MONTH)")
    Long countBatchesByTeacherInPreviousMonth(Long teacherId);

    @Query("SELECT new io.app.dto.Projections.BatchProjection(" +
            "b.id,b.name,b.startYear,b.endYear,b.startMonth," +
            "b.endMonth,b.startTime,b.endTime,b.days," +
            "b.monthlyFees,b.monthlyExamFees) FROM Batch b WHERE" +
            " b.teacher.id=:teacherId")
    List<BatchProjection> findByTeacherWithSpecificDetails(@Param("teacherId") long teacherId);


    Optional<Batch> findByIdAndTeacher(long batchId,Teacher teacher);

}
