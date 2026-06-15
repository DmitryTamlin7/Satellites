package satellite.service;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import satellite.dto.SatelliteEvent;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "satellite-events";

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void  sendSatelliteEvent(SatelliteEvent event){
        kafkaTemplate.send(TOPIC, String.valueOf(event.getId()), event);
        System.out.println("Событие отправлено в Кафку: " + event.getEventType() + "Satellites:  " + event.getName());
    }
}
