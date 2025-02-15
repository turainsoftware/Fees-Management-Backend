package io.app.repository;

import io.app.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateReposiroty extends JpaRepository<State,Long> {
}
