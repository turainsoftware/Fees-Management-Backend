package io.app.repository;

import io.app.model.Teacher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    public Optional<Teacher> findByPhone(String phone);
    public boolean existsByPhone(String phone);
    @Cacheable(value = "teachers",key = "#phone")
    public Optional<Teacher> findById(Long id);
}
