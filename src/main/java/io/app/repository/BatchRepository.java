package io.app.repository;

import io.app.model.Batch;
import io.app.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BatchRepository extends JpaRepository<Batch,Long> {
    List<Batch> findByTeacher(Teacher teacher);
    boolean existsByName(String name);
}
