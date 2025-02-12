package io.app.repository;

import io.app.dto.FeesSummary;
import io.app.dto.TeacherFeesHistoryDto;
import io.app.model.FeesHistory;
import io.app.model.Teacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeesHistoryRepository extends JpaRepository<FeesHistory,Long> {

    @Query("SELECT new io.app.dto.TeacherFeesHistoryDto(" +
            "fh.id, fh.amountPaid, fh.year, fh.month, fh.paymentDate, fh.description, s.name, b.name) " +
            "FROM FeesHistory fh " +
            "JOIN fh.fees f " +
            "JOIN f.batch b " +
            "JOIN f.student s " +
            "WHERE b.teacher.phone = :phone " +
            "ORDER BY fh.paymentDate DESC") //

    List<TeacherFeesHistoryDto> findFeesHistoryByTeacherPhone(@Param("phone") String phone);

    @Query("SELECT new io.app.dto.TeacherFeesHistoryDto(" +
            "fh.id, fh.amountPaid, fh.year, fh.month, fh.paymentDate, fh.description, s.name, b.name) " +
            "FROM FeesHistory fh " +
            "JOIN fh.fees f " +
            "JOIN f.batch b " +
            "JOIN f.student s " +
            "WHERE b.teacher.phone = :phone " +
            "ORDER BY fh.paymentDate DESC") //
    List<TeacherFeesHistoryDto> find10FeesHistoryByTeacherPhone(@Param("phone") String phone, Pageable pageable);


    @Query("SELECT NEW io.app.dto.FeesSummary(YEAR(fh.paymentDate), MONTH(fh.paymentDate), SUM(fh.amountPaid)) " +
            "FROM FeesHistory fh " +
            "WHERE fh.fees.batch.teacher = :teacher " +
            "AND (" +
            "   (YEAR(fh.paymentDate) = :currentYear AND MONTH(fh.paymentDate) = :currentMonth) " +
            "   OR " +
            "   (YEAR(fh.paymentDate) = :previousYear AND MONTH(fh.paymentDate) = :previousMonth)" +
            ") " +
            "GROUP BY YEAR(fh.paymentDate), MONTH(fh.paymentDate)")
    List<FeesSummary> findFeesSummaryByTeacherAndMonths(
            @Param("teacher") Teacher teacher,
            @Param("currentYear") int currentYear,
            @Param("currentMonth") int currentMonth,
            @Param("previousYear") int previousYear,
            @Param("previousMonth") int previousMonth);

}
