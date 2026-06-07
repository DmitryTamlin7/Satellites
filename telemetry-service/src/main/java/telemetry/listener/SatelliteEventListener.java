package telemetry.listener;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import telemetry.domain.InboxRecord;
import telemetry.dto.EventType;
import telemetry.dto.SatelliteEvent;
import telemetry.repository.InboxRepository;
import telemetry.service.TelemetryService;

import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class SatelliteEventListener {
    private final InboxRepository inboxRepository;
    private final TelemetryService telemetryService;

    @Transactional
    @KafkaListener(topics = "satellite-events", groupId = "telemetry-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleSatellitesEvent(SatelliteEvent event) {
        log.info("Проверка сообщений из кафки INBOX " + event.getEventType());

        if (inboxRepository.existsById(event.getEventId())){
            log.info("Дубликат найден событие игнорируется");
            return;
        }

        try {
            if (event.getEventType() == EventType.CREATED) {
                telemetryService.registerSatellite(event.getId(), event.getName());
            } else if (event.getEventType() == EventType.DELETED) {
                telemetryService.unregisterSatellite(event.getId(), event.getName());
            }

            InboxRecord inboxRecord = new InboxRecord(
                    event.getEventId(),
                    event.getId(),
                    event.getEventType().toString(),
                    LocalDateTime.now()
            );
            inboxRepository.save(inboxRecord);
            log.info("Событие обработано");
        }
        catch (Exception e){
            log.info("Ошибка при обработке события");
        }

    }


}
