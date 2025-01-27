package io.app.repository;

import io.app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
    public boolean existsByPhone(String phone);
}
