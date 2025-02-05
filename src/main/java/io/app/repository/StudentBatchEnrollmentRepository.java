package io.app.repository;

import io.app.model.StudentBatchEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentBatchEnrollmentRepository extends JpaRepository<StudentBatchEnrollment,Long> {
}
