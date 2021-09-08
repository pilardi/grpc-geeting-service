package org.geetingservice;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.geetingservice.GreetingServiceOuterClass.HelloRequest;
import org.geetingservice.GreetingServiceOuterClass.HelloResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import io.grpc.internal.testing.StreamRecorder;
import net.devh.boot.grpc.server.autoconfigure.GrpcHealthServiceAutoConfiguration;

@SpringBootTest
@SpringJUnitConfig(classes = { GreetingServiceImplBootTest.Config.class })
@RunWith(SpringRunner.class)
public class GreetingServiceImplBootTest {

    @Configuration
    @ImportAutoConfiguration({GrpcHealthServiceAutoConfiguration.class})
    public static class Config {
        
        @Bean
        public GreetingServiceImpl greetingServiceGrpc() {
            return new GreetingServiceImpl();
        }

    }
    
    @Autowired
    GreetingServiceImpl greetingServiceGrpc;
    
    @Test
    public void testGreeting() throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName("test").build();
        StreamRecorder<HelloResponse> recorder = StreamRecorder.create();
        this.greetingServiceGrpc.greeting(request, recorder);
        if (!recorder.awaitCompletion(3, TimeUnit.SECONDS)) {
            Assert.fail("Enlapsed time without response");
        }
        List<HelloResponse> values = recorder.getValues();
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        HelloResponse response = values.get(0);
        Assert.assertEquals("Hello test!", response.getGreeting());
    }

}
