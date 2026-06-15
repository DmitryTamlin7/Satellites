package satellite.domain;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@DiscriminatorValue("IMAGE")
@Getter @Setter
public class ImagingSatelite extends Satellite {

    private double resolution;
    private int photosTaken;

    public ImagingSatelite(String name, double resolution, EnergySystem energy) {
        super(name, energy);
        this.resolution = resolution;
        this.photosTaken = 0;
    }

    public int getPhotosTaken() {
        return photosTaken;
    }

    public double getResolution() {
        return resolution;
    }

    @Override
    public void performMission() {
        if (energy.getBatteryLevel() < 0.20){
            System.out.println("Сьемка невозмоджна аппарат имеет критический уровень заряда");
            deactivate();
        }
        if (state.isActive()) {
            System.out.printf("%s: Сьемка территории с разрешением %.1f м.пиксель\n", getName(), getResolution());
            TakePhoto();
            consumeEnergy(0.08);
        }else System.out.printf(" %s: сьемка невозможна - аппарат не активен\n", getName());
    }

    public void TakePhoto(){
        if (state.isActive()){
            photosTaken += 1;
            System.out.printf("Снимок: %s сделан!\n", getPhotosTaken());
        }
    }

    @Override
    public String toString() {
        return String.format("%s (Заряд: %d%%)", getName(), (int)(getBatteryLevel() * 100));
    }
}
