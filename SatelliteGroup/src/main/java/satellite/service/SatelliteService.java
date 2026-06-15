package satellite.service;

import satellite.domain.Satellite;
import satellite.factory.SatelliteParam;

public interface SatelliteService {
    Satellite createSatellite(SatelliteParam param);
}
