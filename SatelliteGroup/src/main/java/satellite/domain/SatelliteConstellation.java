package satellite.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "constellations")
@Getter @Setter
@NoArgsConstructor
public class SatelliteConstellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String constellationName;

    @JsonIgnore
    @OneToMany(mappedBy = "constellation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Satellite> satellites;

    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
        this.satellites = new ArrayList<>();
    }

    public void addSatellite(Satellite satellite){
        satellites.add(satellite);
        satellite.setConstellation(this);
    }

    public void executeAllMission() {
        System.out.printf("\nВыполенение миссий Группировки %s\n",
                constellationName.toUpperCase());
        for (Satellite s : satellites){
            s.performMission();
        }
    }

    public void ActivateAllSatellites(){
        System.out.println("\nАктивация спутников\n");
        for (Satellite s : satellites){
            boolean activaited = s.activate();
            if (activaited){
                System.out.printf("✅ %s: Активация успешна\n", s.getName());
            }else {
                System.out.printf("❌ %s; Ошибка Активации (заряд: %d%%)\n", s.getName(), (int) (s.getBatteryLevel() * 100));
            }
        }
    }

}
