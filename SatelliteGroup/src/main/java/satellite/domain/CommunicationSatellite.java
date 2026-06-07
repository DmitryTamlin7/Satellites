package satellite.domain;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@NoArgsConstructor
@DiscriminatorValue("COMMUNICATION")
@Getter
@Setter
public class CommunicationSatellite extends Satellite {

    private  double bandWidth;

    public CommunicationSatellite(String name, double bandWidth, EnergySystem energy) {
        super(name, energy);
        this.bandWidth = bandWidth;
    }

    @Override
    public void performMission() {
        if (state.isActive()){
            System.out.printf("%s: Передача данных со скоростью %.1f Мбит/с\n", getName(), bandWidth);
            sendData(bandWidth);
            consumeEnergy(0.05);
        }
        else {
            System.out.printf("❌ %s: Не может передать данные - не автивен\n", getName());
        }

    }

    public void sendData(double amount){
            System.out.printf("%s: Отправил %.1f Мбит данных!\n", getName(), amount);
    }

    @Override
    public String toString() {
        return String.format("%s (Заряд: %d%%)", getName(), (int)(getBatteryLevel() * 100));
    }


}
