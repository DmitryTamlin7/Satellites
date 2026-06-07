package satellite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import satellite.domain.Satellite;
import satellite.domain.SatelliteConstellation;

import java.util.List;
import java.util.Optional;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {

    Optional<Satellite> findByName(String name);
    List<Satellite> findAllByConstellationConstellationName(String name);
}
