package io.app.repository;

import io.app.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {
    public boolean existsByName(String name);
    public Optional<Subject> findByName(String name);
    public List<Subject> findByNameContaining(String name);
}
