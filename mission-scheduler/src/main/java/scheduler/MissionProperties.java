package scheduler;


import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "app.satellite")
public record MissionProperties (
    String url,
    List<MissionConfig> missions
    ){
    public record MissionConfig(
            String constellationName,
            String cron,
            String satelliteName,
            double batteryLevel,
            double bandWidth
    )
    {}
}


