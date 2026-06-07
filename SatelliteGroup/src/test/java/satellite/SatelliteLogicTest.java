package satellite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import satellite.domain.CommunicationSatellite;
import satellite.domain.EnergySystem;
import satellite.domain.ImagingSatelite;
import satellite.domain.SatelliteConstellation;
import satellite.repository.ConstellationRepository;
import satellite.service.ConstellationService;
public class SatelliteLogicTest {

    private EnergySystem createEnergy(double level) {
        return EnergySystem.builder()
                .batteryLevel(level)
                .maxBattery(1.0)
                .minBattery(0.0)
                .criticleLevel(0.20)
                .build();
    }

    @Test
    @DisplayName("Низкий заряд спутника: не должен быть активен")
    void shouldNotActivateWithLowBattery() {
        CommunicationSatellite sat = new CommunicationSatellite("Action", 10.0, createEnergy(0.15));
        Assertions.assertFalse(sat.isActive());
        boolean activated = sat.activate();
        Assertions.assertFalse(activated);
    }

    @Test
    @DisplayName("Спутник съемки должен считать количество фото")
    void imagingSatMissionTest() {
        ImagingSatelite img = new ImagingSatelite("Фото", 1080.0, createEnergy(0.88));


        img.activate();
        img.performMission();
        img.performMission();

        Assertions.assertEquals(2, img.getPhotosTaken());
        Assertions.assertTrue(img.getBatteryLevel() < 0.88);
    }
}