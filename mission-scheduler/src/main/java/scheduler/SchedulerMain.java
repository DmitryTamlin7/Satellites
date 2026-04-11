package scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(MissionProperties.class)
public class SchedulerMain {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerMain.class, args);
        System.out.println("Запуск планировщика на 8081 порту УСПЕШНО");
    }
}