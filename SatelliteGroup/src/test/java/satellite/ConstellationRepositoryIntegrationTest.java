package satellite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import satellite.domain.Satellite;
import satellite.domain.SatelliteConstellation;
import satellite.factory.CommunicationSatelliteParam;
import satellite.factory.SatelliteParam;
import satellite.repository.ConstellationRepository;
import satellite.service.ConstellationService;
import satellite.service.SatelliteServiceImpl;

@SpringBootTest
@Transactional
public class ConstellationRepositoryIntegrationTest {
    @Autowired
    private ConstellationService service;
    @Autowired
    private ConstellationRepository repository;
    @Autowired
    private SatelliteServiceImpl satelliteService;

    @Test
    @DisplayName("Полный тест: жизненный цикл группировки")
    void fullTest(){
        String name = "Kalinka";

        service.createAndSaveConstellation(name);

        SatelliteParam param = new CommunicationSatelliteParam("Пушкин", 0.55, 50.0);
        Satellite com = satelliteService.createSatellite(param);


        service.addSatelliteToGroup(name, com);
        satelliteService.activateAllInConstellation(name);

        SatelliteConstellation result = repository.findByConstellationName(name)
                .orElseThrow();

        Assertions.assertEquals(1, result.getSatellites().size());
        Assertions.assertTrue(result.getSatellites().get(0).isActive());
    }
}