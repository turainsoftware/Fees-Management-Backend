package io.app.repository;

import io.app.dto.DistrictDto;
import io.app.model.Districts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<Districts,Long> {

    @Query("SELECT new io.app.dto.DistrictDto(d.id,d.name) FROM Districts d WHERE d.state.id=:stateId")
    public List<DistrictDto> findDistrictByStateId(@Param("stateId") long stateId);

}
