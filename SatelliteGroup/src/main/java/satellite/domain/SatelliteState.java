package satellite.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
public class SatelliteState {
    private boolean isActive;

    public void activate(){
        isActive = true;
    }
    public void deactivate(){
        isActive = false;
    }
}
