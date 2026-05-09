package telemetry;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@GrpcService
public class TelemetryServiceImpl extends TelemetryServiceGrpc.TelemetryServiceImplBase {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new Random();

    @Override
    public void streamTelemetry(TelemetryRequest request, StreamObserver<TelemetryResponse> responseObserver) {
        String satelliteId = request.getSatelliteId();
        System.out.println("Начата трансляция телеметрии для: " + satelliteId);

        executorService.scheduleAtFixedRate(() ->{
            try {
                double cpuTemp = 30 + (50) * random.nextDouble();
                double extTemp = -130 + (50) * random.nextDouble();

                TelemetryResponse response = TelemetryResponse.newBuilder()
                        .setCpuTemperature(cpuTemp)
                        .setExternalTemperature(extTemp)
                        .build();
                responseObserver.onNext(response);
            }
            catch (Exception e){
                responseObserver.onError(e);
            }
        }, 1, 2, TimeUnit.SECONDS );
    }
}
