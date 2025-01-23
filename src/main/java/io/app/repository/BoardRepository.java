package io.app.repository;

import io.app.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    public boolean existsByName(String name);
    public Optional<Board> findByName(String name);
    public List<Board> findByNameContaining(String name);
}
