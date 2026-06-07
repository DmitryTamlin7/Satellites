package satellite.factory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import satellite.domain.SatelliteType;

@Setter
@Getter
@NoArgsConstructor
public class ImagingSatelliteParam extends SatelliteParam {
    private double resolution;

    public ImagingSatelliteParam(String name, double batteryLevel, double resolution){
        super(SatelliteType.IMAGE, name, batteryLevel);
        this.resolution = resolution;
    }
}
