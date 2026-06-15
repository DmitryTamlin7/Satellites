package satellite.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import satellite.repository.SatelliteRepository;
import telemetry.TelemetryRequest;
import telemetry.TelemetryResponse;
import telemetry.TelemetryServiceGrpc;

@Service
public class TelemetryClientService {

    @GrpcClient("telemetry-service")
    private TelemetryServiceGrpc.TelemetryServiceStub telemetryStub;

    private final SatelliteRepository satelliteRepository;

    public TelemetryClientService(SatelliteRepository satelliteRepository) {
        this.satelliteRepository = satelliteRepository;
    }


    @Transactional
    public void processUpdate(Long satelliteId, TelemetryResponse response) {
        satelliteRepository.findById(satelliteId).ifPresent(satellite -> {
            satellite.setCpuTemperature(response.getCpuTemperature());
            satellite.setExternalTemperature(response.getExternalTemperature());
            satelliteRepository.saveAndFlush(satellite);

            System.out.println(">>> БД ОБНОВЛЕНА для ID " + satelliteId +
                    ": CPU=" + response.getCpuTemperature() +
                    ", EXT=" + response.getExternalTemperature());
        });
    }

    public void subscribeToTelemetry(Long satelliteId, String satelliteName) {
        TelemetryRequest request = TelemetryRequest.newBuilder()
                .setSatelliteId(satelliteName)
                .build();

        telemetryStub.streamTelemetry(request, new StreamObserver<TelemetryResponse>() {
            @Override
            public void onNext(TelemetryResponse response) {
                processUpdate(satelliteId, response);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Ошибка gRPC: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Поток телеметрии завершен.");
            }
        });
    }
}