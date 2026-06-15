package satellite.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import satellite.exeption.SpaceOperationException;
import satellite.domain.Satellite;
import satellite.domain.SatelliteType;
import satellite.factory.CommunicationSatelliteParam;
import satellite.factory.ImagingSatelliteParam;
import satellite.factory.SatelliteFactory;
import satellite.factory.SatelliteParam;
import satellite.repository.SatelliteRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SatelliteServiceImpl implements SatelliteService {

    private final List<SatelliteFactory> factories;
    private final SatelliteRepository repository;


    @Override
    public Satellite createSatellite(SatelliteParam param) {
        if (param == null) {
            throw new SpaceOperationException("Параметры спутника не заданы");
        }
        SatelliteType type = resolveType(param);
        for (SatelliteFactory factory : factories) {
            if (factory.isSatelliteTypeSupported(type)) {
                return factory.createSatelliteWithParameter(param);
            }
        }
        throw new SpaceOperationException("Фабрика типа " + type + " не найдена");
    }

    private SatelliteType resolveType(SatelliteParam param) {
        if (param.getType() != null) {
            return param.getType();
        }
        if (param instanceof CommunicationSatelliteParam) {
            return SatelliteType.COMMUNICATION;
        }
        if (param instanceof ImagingSatelliteParam) {
            return SatelliteType.IMAGE;
        }
        throw new SpaceOperationException("Не удалось определить тип спутника");
    }

    @Transactional
    public void activateAllInConstellation(String constellationName) {
        List<Satellite> satellites = repository.findAllByConstellationConstellationName(constellationName);
        System.out.println("Активация спутников из " + constellationName + "\n");

        satellites.forEach(s->{
            if (s.activate()) {
                System.out.println(s.getName() + " Успешно активирован");
            }
            else {
                System.out.println(s.getName() + " Ошибка активации");
            }
        });
        repository.saveAll(satellites);
    }

    @Transactional
    public void executeMissionsInConstellation(String constellationName){
        List<Satellite> satellites = repository.findAllByConstellationConstellationName(constellationName);
        System.out.println("Выполнение миссий " + constellationName + "\n");

        satellites.forEach(Satellite::performMission);
        repository.saveAll(satellites);
    }

    @Cacheable(value = "satellite", key = "#id")
    public Satellite getSatelliteById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Спутник с id: " + id + " не найден"));
    }

    @Cacheable(value = "satellites", key = "'all'")
    public List<Satellite> getAllSatellites() {
        return repository.findAll();
    }

    @CacheEvict(value = "satellite", key = "#id")
    public Satellite updateSatellite(Long id, Satellite updatedData) {
        Satellite existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Спутник не найден"));
        existing.setName("Sat-Update");
        return repository.save(existing);
    }

    @Cacheable(value = "satellite", key = "#constellationName + '::' + #satelliteName")
    public Optional<Satellite> findByNames(String constellationName, String satelliteName){
        return repository.findByNameAndConstellationConstellationName(constellationName, satelliteName);
    }

}
