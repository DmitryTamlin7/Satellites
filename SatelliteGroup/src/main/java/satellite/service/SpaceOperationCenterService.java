package satellite.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import satellite.dto.AddSatelliteRequest;
import satellite.aspect.LogExecutionTime;
import satellite.dto.MissionRequest;
import satellite.factory.SatelliteParam;



/**
 Сервис по добавлению спутников в группировку + исполенние работ всех миссий представляет собой
 паттерн FACADE который инкапсулирует ConstellationService и SatelliteService
 */

@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {
    private final ConstellationService constellationService;
    private final SatelliteServiceImpl satelliteService;

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

    @Transactional
    public void removeSatellite(String conName, String satName){
        constellationService.removeSatellite(conName, satName);
    }

    public void quicStart(String name, SatelliteParam param){
        constellationService.createAndSaveConstellation(name);
        addSatellite(new AddSatelliteRequest(name, param));
    }
}
