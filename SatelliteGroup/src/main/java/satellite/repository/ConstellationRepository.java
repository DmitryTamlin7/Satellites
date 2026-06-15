package satellite.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import satellite.domain.SatelliteConstellation;

import java.util.Optional;

@Repository
public interface ConstellationRepository extends JpaRepository<SatelliteConstellation, Long> {

    Optional<SatelliteConstellation> findByConstellationName(String constellationName);

    void  deleteByConstellationName(String constellationName);

    boolean existsByConstellationName(String constellationName);

}
