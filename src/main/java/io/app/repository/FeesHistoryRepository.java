package io.app.repository;

import io.app.dto.TeacherFeesHistoryDto;
import io.app.model.FeesHistory;
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

}
