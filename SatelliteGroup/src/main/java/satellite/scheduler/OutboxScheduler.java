package satellite.scheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import satellite.domain.OutboxRecord;
import satellite.dto.SatelliteEvent;
import satellite.repository.OutboxRepository;
import satellite.service.KafkaProducerService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OutboxScheduler {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducerService;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        List<OutboxRecord> pendingRecords = outboxRepository.findByStatusOrderByCreatedAtAsc("PENDING");
        if (pendingRecords.isEmpty()){
            return;
        }
        log.info("Найдено событий для отправки: " + pendingRecords.size());

        for (OutboxRecord record: pendingRecords){
            try {
                SatelliteEvent event = objectMapper.readValue(record.getPayload(), SatelliteEvent.class);
                kafkaProducerService.sendSatelliteEvent(event);
                record.setStatus("SENT");
                outboxRepository.save(record);
            } catch (Exception e) {
                log.error("Ошибка отправки записи: " + record.getId());
                break;
            }
        }
    }
}

