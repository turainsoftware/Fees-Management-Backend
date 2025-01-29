package io.app.repository;

import io.app.model.Teacher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    public Optional<Teacher> findByPhone(String phone);
    public boolean existsByPhone(String phone);
    @Cacheable(value = "teachers",key = "#phone")
    public Optional<Teacher> findById(Long id);


    // Select teacher id By Phone number
    @Query("SELECT t.id from Teacher t WHERE t.phone=:phone")
    public Optional<Long> findIdByPhone(@Param("phone") String phone);
}
