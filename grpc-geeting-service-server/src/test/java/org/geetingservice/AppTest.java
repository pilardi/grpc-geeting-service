package org.geetingservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.geetingservice.GreetingServiceOuterClass.HelloRequest;
import org.geetingservice.GreetingServiceOuterClass.HelloResponse;
import org.junit.Ignore;
import org.junit.Test;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

// TODO upgrade to JUNIT 5
// https://reflectoring.io/spring-boot-test/
@Slf4j
public class AppTest {

    @Ignore // you need the service running
    @Test
    public void testApp() {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
        try {
            // It is up to the client to determine whether to block the call
            // Here we create a blocking stub, but an async stub,
            // or an async stub with Future are always possible.
            GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
            HelloRequest request = HelloRequest.newBuilder().setName("Ray").build();

            // Finally, make the call using the stub
            HelloResponse response = stub.greeting(request);

            log.debug("Response is: {}", response);

            assertEquals("Hello Ray!", response.getGreeting());

        } finally {
            // A Channel should be shutdown before stopping the process.
            channel.shutdownNow();
        }

    }

}
