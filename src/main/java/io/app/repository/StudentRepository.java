package io.app.repository;

import io.app.model.Batch;
import io.app.model.Student;
import io.app.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student,Long> {
    public boolean existsByPhone(String phone);
    public List<Student> findByTeachersOrderByCreatedAtDesc(Teacher teacher);
    public List<Student> findByTeachersOrderByCreatedAtAsc(Teacher teacher);
    public List<Student> findByBatches(Set<Batch> batches);
    public Optional<Student> findByPhone(String phone);
}
