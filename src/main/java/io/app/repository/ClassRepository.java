package io.app.repository;

import io.app.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class,Long> {
    public boolean existsByName(String name);
    public Class findByName(String name);
    public List<Class> findByNameContaining(String name);
}
