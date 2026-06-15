package satellite.service;


import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import satellite.aspect.LogExecutionTime;
import satellite.domain.Satellite;
import satellite.dto.AddSatelliteRequest;
import satellite.dto.MissionRequest;


/**
 Сервис по добавлению спутников в группировку + исполенние работ всех миссий представляет собой
 паттерн FACADE который инкапсулирует ConstellationService и SatelliteService
 */

@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {
    private final ConstellationService constellationService;
    private final SatelliteServiceImpl satelliteService;



    @CacheEvict(value = "satellites", allEntries = true)
    @Transactional
    @LogExecutionTime
    public void addSatellite(AddSatelliteRequest request){
        constellationService.addSatelliteToGroup(request.getConstellationName(), request.getSatelliteParam());
    }





    @LogExecutionTime
    public void executeMission(MissionRequest request){
        satelliteService.activateAllInConstellation(request.getConstellationName());
        satelliteService.executeMissionsInConstellation(request.getConstellationName());
    }

    public void createConstellation(String name){
        constellationService.createAndSaveConstellation(name);
    }

    public String getSystemStatus(String name) {
        return constellationService.showConstellationStatus(name);
    }

    @Caching(evict = {
            @CacheEvict(value = "satellite", key = "#id"),
            @CacheEvict(value = "satellites", allEntries = true)
    })
    @Transactional
    public void removeSatellite(String conName, String satName){
        constellationService.removeSatellite(conName, satName);
    }

}
