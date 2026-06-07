package satellite.factory;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import satellite.domain.SatelliteType;

@Setter
@Getter
@NoArgsConstructor
public class CommunicationSatelliteParam extends SatelliteParam {
    private double bandWidth;

    public CommunicationSatelliteParam(String name, double batteryLevel, double bandWidth){
        super(SatelliteType.COMMUNICATION, name, batteryLevel);
        this.bandWidth = bandWidth;
    }

}
