package telemetry.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelemetryService {

    private final Set<Long> activeSatellites = ConcurrentHashMap.newKeySet();

    public void registerSatellite(Long id, String name) {
        activeSatellites.add(id);
        System.out.println("Спутник " + name + " (ID: " + id + ") зарегистрирован для сбора телеметрии.");
    }

    public void unregisterSatellite(Long id, String name) {
        activeSatellites.remove(id);
        System.out.println("Спутник " + name + " (ID: " + id + ") удален из системы сбора телеметрии.");
    }


    public boolean isSatelliteActive(Long id) {
        return activeSatellites.contains(id);
    }
}