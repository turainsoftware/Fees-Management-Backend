package io.app.repository;

import io.app.model.Batch;
import io.app.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch,Long> {
}
