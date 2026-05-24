package telemetry.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import telemetry.dto.EventType;
import telemetry.dto.SatelliteEvent;

/**
 * Лисенер кафки принимает событие
 */

@Component
public class SatelliteEventListener {

    @KafkaListener(topics = "satellite-events", groupId = "telemetry-group", containerFactory = "kafkaListenerContainerFactory")
    public void  listenerSatellitesEvents(SatelliteEvent event){
        System.out.println("Полученные сообщения");
        System.out.println("   Имя спутника: " + event.getName());
        System.out.println("   Событие: " + event.getEventType());

        if (event.getEventType() == EventType.CREATED) {
            handleSatelliteCreated(event);
        } else if (event.getEventType() == EventType.DELETED) {
            handleSatelliteDeleted(event);
        }
    }

    private void handleSatelliteCreated(SatelliteEvent event){
        System.out.println("Спутник зарегистрирован");
    }

    private void handleSatelliteDeleted(SatelliteEvent event){
        System.out.println("Спутник Удален из сервиса телеметрии");
    }


}
