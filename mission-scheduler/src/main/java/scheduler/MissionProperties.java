package scheduler;


import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "app.satellite")
public record MissionProperties (
    String url,
    List<MissionConfig> missions
    ){
    public record MissionConfig(
            String targetType,
            String constellationName,
            String satelliteName,
            String cron
    )
    {}
}


