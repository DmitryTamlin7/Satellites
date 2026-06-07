package satellite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import satellite.domain.CommunicationSatellite;
import satellite.domain.Satellite;
import satellite.domain.SatelliteType;
import satellite.exeption.SpaceOperationException;
import satellite.factory.CommunicationSatelliteParam;
import satellite.factory.SatelliteParam;
import satellite.service.SatelliteService;

@SpringBootTest(classes = Main.class)
public class SatelliteServiceTest {

    @Autowired
    private SatelliteService satelliteService;

    @Test
    @DisplayName("Сервис автоматически выбирает правильную фабрику для спутника связи")
    void testServiceCreatesCommunicationSatellite() {
        SatelliteParam param = new CommunicationSatelliteParam("Орбита-1", 1.0, 50.0);
        Satellite comsat = satelliteService.createSatellite(param);

        Assertions.assertNotNull(comsat);
        Assertions.assertTrue(comsat instanceof CommunicationSatellite);
        Assertions.assertEquals("Орбита-1", comsat.getName());
        CommunicationSatellite commSat = (CommunicationSatellite) comsat;
        Assertions.assertEquals(50.0, commSat.getBandWidth());
    }

    @Test
    @DisplayName("Ошибка при попытке передать неподдерживаемый параметр")
    void testUnsupportedParamType() {
        SatelliteParam wrongParam = new SatelliteParam(SatelliteType.IMAGE, "Орбита-2", 1.0) {};

        Assertions.assertThrows(SpaceOperationException.class, () -> {
            satelliteService.createSatellite(wrongParam);
        });
    }
}