package satellite.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import satellite.factory.SatelliteParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSatelliteRequest {
    private String constellationName;
    private SatelliteParam satelliteParam;
}
