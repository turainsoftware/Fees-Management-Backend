package io.app.repository;

import io.app.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
    public boolean existsByName(String name);
    public Optional<Language> findByName(String name);
    public List<Language> findByNameContaining(String name);
}
