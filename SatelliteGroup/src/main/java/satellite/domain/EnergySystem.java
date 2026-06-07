package satellite.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "energy_systems")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergySystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double batteryLevel;
    private double maxBattery;
    private double minBattery;
    private double criticleLevel;

    public boolean consumeBattery(double amount) {
        if (!isCriticleLevel()) {
            batteryLevel -= amount;
            if (batteryLevel < minBattery) batteryLevel = minBattery;
            return true;
        }
        return false;
    }

    public void rechargeBattery(double amount) {
        if (batteryLevel + amount <= maxBattery) {
            batteryLevel += amount;
        } else {
            batteryLevel = maxBattery;
        }
    }

    public boolean isCriticleLevel() {
        return batteryLevel <= criticleLevel;
    }
}