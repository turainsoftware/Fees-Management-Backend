package io.app.repository;

import io.app.model.Batch;
import io.app.model.Teacher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch,Long> {
    List<Batch> findByTeacher(Teacher teacher);
    boolean existsByName(String name);
    @Cacheable(value = "batches",key = "#batchId")
    public Optional<Batch> findById(Long id);

    public boolean existsByNameAndTeacher(String name,Teacher teacher);
}
