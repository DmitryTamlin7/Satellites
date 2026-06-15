package satellite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import satellite.domain.CommunicationSatellite;
import satellite.domain.EnergySystem;

public class DomainClassesTest {
    private EnergySystem createEnergy(double level, double critical) {
        return EnergySystem.builder()
                .batteryLevel(level)
                .maxBattery(1.0)
                .minBattery(0.0)
                .criticleLevel(critical)
                .build();
    }

    @Test
    @DisplayName("Критический уровень заряда")
    void testEnergySystem() {
        EnergySystem energy = createEnergy(0.20, 0.10);
        energy.consumeBattery(0.15); // Остаток 0.05

        Assertions.assertEquals(0.05, energy.getBatteryLevel(), 0.001);
        Assertions.assertTrue(energy.isCriticleLevel(), "Заряд 0.05 должен быть критическим при пороге 0.10");
    }

    @Test
    @DisplayName("Satellite: Проверка логики активации и деактивации")
    void testSatelliteBaseLogic() {

        CommunicationSatellite com = new CommunicationSatellite("Зенит", 500.0, createEnergy(0.45, 0.05));
        Assertions.assertTrue(com.activate(), "Спутник должен активироваться при нормальном заряде");

        CommunicationSatellite com2 = new CommunicationSatellite("Зенит22", 500.0, createEnergy(0.02, 0.05));
        Assertions.assertFalse(com2.activate(), "Спутник НЕ должен активироваться при заряде ниже критического");
    }
}