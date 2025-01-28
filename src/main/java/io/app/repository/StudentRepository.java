package io.app.repository;

import io.app.model.Student;
import io.app.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    public boolean existsByPhone(String phone);
    public List<Student> findByTeachers(Teacher teacher);
}
