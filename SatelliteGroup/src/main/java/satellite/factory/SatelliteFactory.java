package satellite.factory;

import satellite.domain.Satellite;
import satellite.domain.SatelliteType;

public interface SatelliteFactory {
    Satellite createSatelliteWithParameter(SatelliteParam param);
    boolean isSatelliteTypeSupported(SatelliteType type);
}
