package org.geetingservice;

import org.geetingservice.GreetingServiceGrpc.GreetingServiceImplBase;
import org.geetingservice.GreetingServiceOuterClass.HelloRequest;
import org.geetingservice.GreetingServiceOuterClass.HelloResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import io.grpc.health.v1.HealthCheckResponse.ServingStatus;
import io.grpc.services.HealthStatusManager;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * - https://www.baeldung.com/grpc-introduction
 * 
 * @see https://yidongnan.github.io/grpc-spring-boot-starter/en/server/getting-started.html
 * @see https://github.com/yidongnan/grpc-spring-boot-starter
 * @see https://github.com/yidongnan/grpc-spring-boot-starter/blob/master/grpc-server-spring-boot-autoconfigure/src/main/java/net/devh/boot/grpc/server/config/GrpcServerProperties.java
 * @see https://blogs.asarkar.com/technical/grpc-kubernetes-spring/
 * @see https://docs.spring.io/spring-boot/docs/2.2.x/reference/html/production-ready-features.html#writing-custom-healthindicators
 * 
 * @author pilardi
 */
@Slf4j
@GrpcService
public class GreetingServiceImpl extends GreetingServiceImplBase implements HealthIndicator, InitializingBean {

    @Autowired
    private HealthStatusManager healthStatusManager;
    
    @Override
    public void greeting(final HelloRequest request, final StreamObserver<HelloResponse> responseObserver) {
        log.debug("Handling {}", request);
        final HelloResponse response = HelloResponse
            .newBuilder()
            .setGreeting(String.format("Hello %s!", request.getName()))
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public Health health() {
        // TODO check service state
        return Health.up().build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.healthStatusManager.setStatus(GreetingServiceGrpc.SERVICE_NAME, ServingStatus.SERVING);
    }

}
