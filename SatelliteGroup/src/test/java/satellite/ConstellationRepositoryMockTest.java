package satellite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import satellite.domain.SatelliteConstellation;
import satellite.repository.ConstellationRepository;
import satellite.service.ConstellationService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ConstellationRepositoryMockTest {

    @Mock
    private ConstellationRepository repositoryMock;

    @InjectMocks
    private ConstellationService service;

    @Test
    @DisplayName("Вызов save при добавлении группировки")
    void testServiceAddsConstellation(){
        service.createAndSaveConstellation("ЮрийГагарин7");
        verify(repositoryMock, times(1)).save(any(SatelliteConstellation.class));
    }

    @Test
    @DisplayName("Проверка существования через репозиторий")
    void testMockReturnValues() {
        String name = "Space1";

        when(repositoryMock.existsByConstellationName(name)).thenReturn(true);
        boolean exist = repositoryMock.existsByConstellationName(name);
        assertTrue(exist);
        verify(repositoryMock).existsByConstellationName(name);
    }
}