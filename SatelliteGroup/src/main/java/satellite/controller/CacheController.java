package satellite.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import satellite.domain.Satellite;
import satellite.domain.SatelliteConstellation;
import satellite.service.ConstellationService;
import satellite.service.SatelliteServiceImpl;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CacheController {

    private final SatelliteServiceImpl satelliteService;
    private final ConstellationService constellationService;


    @GetMapping("/satellites/{id}")
    public Satellite getSatelliteById(@PathVariable Long id) {
        return satelliteService.getSatelliteById(id);
    }

    @GetMapping("/satellites")
    public List<Satellite> getAllSatellites() {
        return satelliteService.getAllSatellites();
    }

    @GetMapping("/constellations/{name}")
    public SatelliteConstellation getConstellationByName(@PathVariable String name) {
        return constellationService.getConstellationByName(name);
    }

    @GetMapping("/satellites/search")
    public Optional<Satellite> getSatelliteByConstellationAndName(
            @RequestParam String constellation,
            @RequestParam String name) {
        return satelliteService.findByNames(constellation, name);
    }


    @PutMapping("/satellites/{id}")
    public Satellite updateSatellite(@PathVariable Long id, @RequestBody Satellite satellite) {
        return satelliteService.updateSatellite(id, satellite);
    }

}