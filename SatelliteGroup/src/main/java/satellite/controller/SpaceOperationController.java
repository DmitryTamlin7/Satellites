package satellite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import satellite.dto.AddSatelliteRequest;
import satellite.dto.MissionRequest;
import satellite.service.SpaceOperationCenterService;

/***
 * Контроллер связывает запросы от HTTP с фасадом
 */
@RestController
@RequestMapping("/api/constellations")
@RequiredArgsConstructor
public class SpaceOperationController {

    private final SpaceOperationCenterService facade;

    @PostMapping
    public ResponseEntity<String> createConstellation(@RequestParam String name){
        facade.createConstellation(name);
        return ResponseEntity.ok("Группировка " + name + " Создана");
    }

    @PostMapping("/satellites")
    public ResponseEntity<String> addSatellites(@RequestBody AddSatelliteRequest request){
        facade.addSatellite(request);
        return ResponseEntity.ok("Cпутник добавлен в группу");
    }

    @PostMapping("/missions")
    public ResponseEntity<String> executeMissions(@RequestBody MissionRequest request){
        facade.executeMission(request);
        return ResponseEntity.ok("Миссия " + request.getConstellationName() + " Запущена");
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getSystemOverview(@PathVariable String name) {
        String status = facade.getSystemStatus(name);
        return ResponseEntity.ok(status);
    }
    @DeleteMapping("/{constellationName}/satellites/{satelliteName}")
    public ResponseEntity<String> decommissionSatellite(
            @PathVariable String constellationName,
            @PathVariable String satelliteName) {
        facade.removeSatellite(constellationName, satelliteName);
        return ResponseEntity.ok("Cпутник " + satelliteName + " из группировки " + constellationName + " выведен из эксплуатации" );
    }
}
