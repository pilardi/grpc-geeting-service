package org.geetingservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.geetingservice.GreetingServiceGrpc.GreetingServiceBlockingStub;
import org.geetingservice.GreetingServiceOuterClass.HelloRequest;
import org.geetingservice.GreetingServiceOuterClass.HelloResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcHealthServiceAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;

@SpringBootTest(properties = {
    "grpc.server.inProcessName=test", // Enable inProcess server
    "grpc.server.port=-1", // Disable external server
    "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
})
@SpringJUnitConfig(classes = GreetingServiceImplGrpcTest.Config.class )
@DirtiesContext
@RunWith(SpringRunner.class)
@Slf4j
public class GreetingServiceImplGrpcTest {
    
    @Configuration
    @ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class, // Create required server beans
        GrpcServerFactoryAutoConfiguration.class, // Select server implementation
        GrpcHealthServiceAutoConfiguration.class,
        GrpcClientAutoConfiguration.class}) // Support @GrpcClient annotation
    public static class Config {
        
        @Bean
        GreetingServiceImpl GreetingServiceImpl() {
            return new GreetingServiceImpl();
        }

    }

    @GrpcClient("inProcess")
    private GreetingServiceBlockingStub myService;

    @Test
    public void testApp() {
        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
        HelloRequest request = HelloRequest.newBuilder().setName("Ray").build();

        // Finally, make the call using the stub
        HelloResponse response = this.myService.greeting(request);

        log.debug("Response is: {}", response);

        assertEquals("Hello Ray!", response.getGreeting());
    }

}
