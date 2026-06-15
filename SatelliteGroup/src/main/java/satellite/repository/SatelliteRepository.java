package satellite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import satellite.domain.Satellite;
import satellite.domain.SatelliteConstellation;

import java.util.List;
import java.util.Optional;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {

    Optional<Satellite> findByName(String name);
    List<Satellite> findAllByConstellationConstellationName(String name);
    Optional<Satellite> findByNameAndConstellationConstellationName(String name, String conName);
}

