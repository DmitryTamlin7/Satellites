package satellite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("🚀 СЕРВЕР УПРАВЛЕНИЯ СПУТНИКАМИ ЗАПУЩЕН НА ПОРТУ 8080");
        System.out.println("📖 Документация API: http://localhost:8080/swagger-ui.html");
    }
}