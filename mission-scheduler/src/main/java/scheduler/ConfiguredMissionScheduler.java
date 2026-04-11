package scheduler;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Objects;

@Service
public class ConfiguredMissionScheduler {

    private final TaskScheduler scheduler;
    private final MissionProperties missionProp;
    private final  RestClient client;

    public ConfiguredMissionScheduler(TaskScheduler scheduler, MissionProperties missionProp) {
        this.scheduler = scheduler;
        this.missionProp = missionProp;
        this.client = RestClient.builder().baseUrl(missionProp.url()).build();
    }

    @PostConstruct
    public void  initAndSheldureMissions(){
        for (MissionProperties.MissionConfig mission : missionProp.missions()){
            String name = mission.constellationName();
            createConstellationOnServer(name);
            addSatellite(name);
            sheldureMissions(mission);
        }
    }

    private void addSatellite(String constellationName){
        System.out.println("Добавление спутника в группировку");
        try {
            Map<String, Object> satelliteParam = Map.of(
                    "type", "IMAGE",
                    "name", "Покров-5",
                    "batteryLevel", 1,
                    "bandWidth", 5.0
            );

            Map<String, Object> requestBody = Map.of(
                    "constellationName", constellationName,
                    "satelliteParam", satelliteParam
            );
            client.post()
                    .uri("/api/add-satellites")
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();
            System.out.println(" Спутник успешно выведен на орбиту");
        } catch (Exception e) {
            System.out.println(" Не удалось добавить спутник: " + e.getMessage());
        }
    }

    private void createConstellationOnServer(String name){
        System.out.println("Инициализация: Создание группировки: " + name);
        try {
            client.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/constellations")
                            .queryParam("name", name)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
            System.out.printf("Группировка %s успешно создана", name);
        }
        catch (Exception e){
            System.out.println("ERROR Группировка не создана");
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void sheldureMissions(MissionProperties.MissionConfig mission) {
        scheduler.schedule(() -> {
            System.out.println("Запуск команды на миссию " + mission.constellationName());
            try {
                Map<String, String> requestBody = Map.of("constellationName", mission.constellationName());

                client.post()
                        .uri("/api/missions")
                        .body(requestBody)
                        .retrieve()
                        .toBodilessEntity();
                System.out.println("POST запрос отправлен");
            } catch (Exception e) {
                System.out.println("Ошибка при Post Запросе");
                System.out.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }
        }, new CronTrigger(mission.cron()));

        scheduler.schedule(() -> {
            System.out.println("Запрос статуса для " + mission.constellationName());
            try {
                client.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/overview")
                                .queryParam("name", mission.constellationName())
                                .build())
                        .retrieve()
                        .toBodilessEntity();
                System.out.println("GET запрос на статус отправлен");
            } catch (Exception e) {
                System.out.println("Ошибка при запросе статуса: " + e.getMessage());
            }
        }, new CronTrigger("*/15 * * * * *"));

        System.out.println("Запланирована новая миссия группировки " + mission.constellationName());
    }


}
