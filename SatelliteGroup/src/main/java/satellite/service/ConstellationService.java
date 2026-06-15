package satellite.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import satellite.aspect.LogExecutionTime;
import satellite.domain.OutboxRecord;
import satellite.domain.Satellite;
import satellite.domain.SatelliteConstellation;
import satellite.dto.EventType;
import satellite.dto.SatelliteEvent;
import satellite.factory.SatelliteParam;
import satellite.repository.ConstellationRepository;
import org.springframework.transaction.annotation.Transactional;
import satellite.repository.OutboxRepository;
import satellite.repository.SatelliteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConstellationService {
    private final ConstellationRepository repository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final SatelliteServiceImpl satelliteService;
    private final SatelliteRepository satelliteRepository;


    @Transactional
    @LogExecutionTime
    public void createAndSaveConstellation(String name){
        if (repository.existsByConstellationName(name)){
            throw new RuntimeException("Группировка с таким именем уже есть");
        }
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        repository.save(constellation);
        System.out.println("Группировка " + constellation.getConstellationName() + " Сохранена в БД");
    }

    @Caching(evict = {
            @CacheEvict(value = "constellation", key = "#name"),
            @CacheEvict(value = "satellites", allEntries = true)
    })
    @Transactional
    @LogExecutionTime
    public void addSatelliteToGroup(String groupName, Satellite satellite){
        SatelliteConstellation constellation = repository.findByConstellationName(groupName)
                .orElseThrow(() -> new RuntimeException("Группировка не найдена: " + groupName));

        constellation.addSatellite(satellite);
        repository.save(constellation);
    }

    @Transactional
    @LogExecutionTime
    public SatelliteConstellation getConstellation(String name){
        return repository.findByConstellationName(name)
                .orElseThrow(() -> new RuntimeException("Группировки не существует"));
    }

    public List<SatelliteConstellation> getAll(){
        return repository.findAll();
    }

    @Transactional
    @LogExecutionTime
    public void removeConstellation(String name){
        if (!repository.existsByConstellationName(name)){
            throw new RuntimeException("Нет такой группировки нечего удалять");
        }
        repository.deleteByConstellationName(name);
        System.out.println("Группировка " + name + " Удалена");
    }

    @Transactional
    @LogExecutionTime
    public void addSatelliteToGroup(String constellationName, SatelliteParam param){
        SatelliteConstellation constellation = getConstellation(constellationName);
        Satellite satellite = satelliteService.createSatellite(param);
        constellation.addSatellite(satellite);
        satellite = satelliteRepository.saveAndFlush(satellite);
        repository.save(constellation);

        Long satelliteId = satellite.getId();

        UUID eventId = UUID.randomUUID();
        SatelliteEvent event = new SatelliteEvent(
                eventId,
                satelliteId,
                satellite.getName(),
                EventType.CREATED
        );

        try {
            String jsonPayload = objectMapper.writeValueAsString(event);

            OutboxRecord outboxRecord = new OutboxRecord(
                    satelliteId,
                    "CREATED",
                    jsonPayload,
                    LocalDateTime.now(),
                    "PENDING"
            );
            outboxRepository.save(outboxRecord);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении события CREATED в Outbox: " + e.getMessage(), e);
        }

        System.out.println("Спутник " + satellite.getName() + " успешно добавлен (событие CREATED сохранено в Outbox)");
    }

    @Caching(evict = {
            @CacheEvict(value = "constellation", key = "#name"),
            @CacheEvict(value = "satellites", allEntries = true)
    })
    @Transactional
    @LogExecutionTime
    public void removeSatellite(String conName, String satName){
        SatelliteConstellation constellation = getConstellation(conName);
        Satellite satelliteToDelete = constellation.getSatellites().stream()
                .filter(s -> s.getName().equals(satName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Спутник не найден в группировке"));
        Long deletedId = satelliteToDelete.getId();
        constellation.getSatellites().remove(satelliteToDelete);
        repository.saveAndFlush(constellation);
        UUID eventId = UUID.randomUUID();

        SatelliteEvent event = new SatelliteEvent(
                eventId,
                deletedId,
                satName,
                EventType.DELETED
        );

        try {
            String jsonPayLoad = objectMapper.writeValueAsString(event);
            OutboxRecord outboxRecord = new OutboxRecord(
                    deletedId,
                    "DELETED",
                    jsonPayLoad,
                    LocalDateTime.now(),
                    "PENDING"
            );
            outboxRepository.save(outboxRecord);
        } catch (Exception e) {
            throw new RuntimeException("ОШибка при создании события + ", e);
        }

        System.out.println("Спутник " + satName + " успешно выведен из группировки");
    }

    public String showConstellationStatus(String name) {
        SatelliteConstellation constellation = repository.findByConstellationName(name)
                .orElseThrow(() -> new RuntimeException("Группировка не найдена: " + name));
        StringBuilder sb = new StringBuilder();
        sb.append("=== СТАТУС ГРУППИРОВКИ: ").append(constellation.getConstellationName()).append(" ===\n");
        sb.append("Количество спутников: ").append(constellation.getSatellites().size()).append("\n");

        constellation.getSatellites().forEach(s ->
                sb.append(s.getBaseDetails()).append("\n")
        );
        return sb.toString();
    }


    @Transactional
    @LogExecutionTime
    @Cacheable(value = "constellation", key = "#name")
    public SatelliteConstellation getConstellationByName(String name){
        return repository.findByConstellationName(name)
                .orElseThrow(() -> new RuntimeException("Группировки с именем: " + name + " Нет"));
    }
}


