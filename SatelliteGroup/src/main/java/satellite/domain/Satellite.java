package satellite.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "satellites")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "satellite_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Satellite{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    protected SatelliteState state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "energy_id")
    protected EnergySystem energy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constellation_id")
    private SatelliteConstellation constellation;

    @Column(name = "cpu_temperature")
    private Double cpuTemperature;

    @Column(name = "external_temperature")
    private Double externalTemperature;


    public Satellite(String name, EnergySystem energy) {
        this.name = name;
        this.energy = energy;
        this.state = new SatelliteState();
    }


    public boolean activate(){
        if (!energy.isCriticleLevel()){
            state.activate();
            return true;
        }
        return false;
    }

    public void deactivate(){
        state.deactivate();
    }

    protected void consumeEnergy(double amount){
        energy.consumeBattery(amount);
        if (energy.isCriticleLevel()){
            deactivate();
        }
    }

    public abstract void performMission();

    public double getBatteryLevel() {
        return energy.getBatteryLevel();
    }

    public Boolean isActive() {
        return state.isActive();
    }

    @Override
    public String toString() {
        return String.format("%s (Заряд: %d%%)", name, (int)(getBatteryLevel() * 100));
    }

    public String getBaseDetails() {
        return String.format("name='%s', isActive=%s, batteryLevel=%.2f",
                name, state.isActive(), energy.getBatteryLevel());
    }
}
